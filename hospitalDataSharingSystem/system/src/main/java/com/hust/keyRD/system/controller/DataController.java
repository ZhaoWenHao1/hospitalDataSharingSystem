package com.hust.keyRD.system.controller;

import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.exception.mongoDB.MongoDBException;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.utils.MD5Util;
import com.hust.keyRD.commons.vo.UploadResult;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.file.service.FileService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@Api(tags = "文件管理")
public class DataController {
    @Resource
    private DataService dataService;
    @Resource
    private FabricService fabricService;
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    @Resource
    private FileService fileService;

    //上传文件
    @CheckToken
    @PostMapping(value = "/data/uploadFile/{channelId}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    // Transactional注解默认在抛出uncheck异常（继承自Runtime Exception或 Error ）时才会回滚 而IO SQL等异常属于check异常，所以不会回滚
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("channelId") Integer channelId, HttpServletRequest httpServletRequest) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        User user = userService.findUserById(originUserId);
        Channel channel = channelService.findChannelById(user.getChannelId());
        Channel dstChannel = channelService.findChannelById(channelId);
        try {
            // 文件保存到mongoDB
            FileModel f = new FileModel(file.getOriginalFilename(), file.getContentType(), file.getSize(),
                    new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            fileService.saveFile(f);

            DataSample dataSample = new DataSample();
            dataSample.setChannelId(channelId);//这里后面要做出选择channel
            dataSample.setDataName(fileName);
            //文件大小以KB作为单位
            // 首先先将.getSize()获取的Long转为String 单位为B
            Double size = Double.parseDouble(String.valueOf(file.getSize()));
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 此时size就是保留两位小数的浮点数
            dataSample.setDataSize(size);
            dataSample.setMongoId(f.getId());
            dataSample.setOriginUserId(originUserId);
            dataSample.setDataType(fileName.substring(fileName.lastIndexOf(".")) + "文件");
            //初次创建时将初始时间和修改时间写成一样
            dataSample.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            dataSample.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            dataService.uploadFile(dataSample);
            log.info("************fabric上传文件操作记录区块链开始*****************");
            UploadResult result = new UploadResult();
            // 1. 权限申请 一次上链
            String username = user.getUsername();
            //String dstChannelName = dstChannel.getChannelName();
            //String srcChannelName = channelService.findChannelById(user.getChannelId()).getChannelName();
            String txId = fabricService.applyForCreateFile(username, channel.getChannelName(),dataSample.hashCode()+"", dataSample.getId() + "");
            log.info("1.创建文件成功 txId: " + txId);
            result.setFirstUpChainTx(txId);
//            //hash
//            // 3. 更新链上hash 二次上链
            String record = fabricService.updateForCreateFile(username, channel.getChannelName(),dataSample.hashCode()+"", dataSample.getId() + "", txId);
            log.info("2. 更新链上hash ： " + record);
            result.setSecondUpChainTx(record);
//            // 4. 授予用户文件的查改权限
            Boolean res = false;
            if(channel.getId()==1){
                res = fabricService.grantUserPermissionOnFileInnerChannel("org2_admin",dataSample.getId() + "", channel.getChannelName(), "read", "role1", username);
            }else if(channel.getId()==2){
                res = fabricService.grantUserPermissionOnFileInnerChannel("org4_admin",dataSample.getId() + "", channel.getChannelName(), "read", "role1", username);
            }
           log.info("3.授予用户文件读取权限：" + res);
            result.setGrantReadRes(res.toString());
            if(channel.getId()==1){
                res = fabricService.grantUserPermissionOnFileInnerChannel("org2_admin",dataSample.getId() + "", channel.getChannelName(), "modify", "role1", username);
            }else if(channel.getId()==2){
                res = fabricService.grantUserPermissionOnFileInnerChannel("org4_admin",dataSample.getId() + "", channel.getChannelName(), "modify", "role1", username);
            }
           log.info("4.授予用户文件修改权限：" + res);
            result.setGrantModifyRes(res.toString());
            //写入上传者权限
         log.info("************fabric上传文件操作记录区块链结束*****************");
            return new CommonResult<>(200, "文件上传成功", result);
        } catch (Exception e) {
            return new CommonResult<>(400, e.getMessage(), null);
        }
    }

    //获取文件列表  //这里获取所有通道的所有文件
    @CheckToken
    @GetMapping(value = "/data/getDataList")
    public CommonResult getDataList(HttpServletRequest httpServletRequest) {

        return new CommonResult<>(200, "获取该用户所有文件权限列表成功", null);
    }


    //根据文件id获取文件内容
    @CheckToken
    @PostMapping(value = "/data/getData")
    @ResponseBody
    // TODO
    public CommonResult getData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        Integer dataId = Integer.valueOf(params.get("dataId"));
        DataSample dataSample = dataService.findDataById(dataId);
        DataSample result = dataService.findDataById(dataId);
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        User user = userService.findUserById(originUserId);
        if (result == null) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        log.info("************fabric读取文件操作记录区块链开始*****************");
        // 1. 申请读取权限
        String username = user.getUsername();
        String dstChannelName = channelService.findChannelById(dataSample.getChannelId()).getChannelName();
        String srcChannelName = channelService.findChannelById(user.getChannelId()).getChannelName();
        String txId = fabricService.applyForReadFile(username, srcChannelName, dataSample.hashCode()+"", String.valueOf(dataId));
        if (txId == null || txId.isEmpty()) {
            log.info("申请文件读取权限失败");
            return new CommonResult<>(300, "申请文件读取权限失败", null);
        }
        // 2. 读取文件
        String fileContent = new String(Objects.requireNonNull(fileService.getFileById(dataSample.getMongoId())
                .map(FileModel::getContent)
                .map(Binary::getData)
                .orElse(null))
        );
        // 3. 二次上链
        String record = fabricService.updateForReadFile(username, srcChannelName, dataSample.hashCode()+"", String.valueOf(dataId), txId);
        log.info("2. 二次上链 ： " + record);
        log.info("************fabric读取文件操作记录区块链结束*****************");
        return new CommonResult<>(200, "文件token为：" + token + "\r\ntxId：" + txId, fileContent);
    }


    //根据文件id对文件内容进行更新
    @CheckToken
    @PostMapping(value = "/data/updateData")
    @ResponseBody
    @Transactional
    // TODO
    public CommonResult updateData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        User user = userService.findUserById(originUserId);
        Integer dataId = Integer.valueOf(params.get("dataId"));
        String dataContent = params.get("dataContent");
        DataSample dataSample = dataService.findDataById(dataId);
        if (dataSample == null) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        File old_file = new File(dataSample.getDataName());
        log.info("************fabric更新文件操作记录区块链开始*****************");
        // 1. 申请文件修改权限
        String username = user.getUsername();
        String dstChannelName = channelService.findChannelById(dataSample.getChannelId()).getChannelName();
        String srcChannelName = channelService.findChannelById(user.getChannelId()).getChannelName();
        String txId = fabricService.applyForModifyFile(username, srcChannelName,dataSample.hashCode()+"", String.valueOf(dataId));
        if (txId == null || txId.isEmpty()) {
            log.info("申请文件修改权限失败");
            return new CommonResult<>(300, "申请文件修改权限失败", null);
        }
        // 2. 修改文件
        // 更新mongoDB
        FileModel fileModel = fileService.getFileById(dataSample.getMongoId()).orElseThrow(MongoDBException::new);
        fileModel.setContent(new Binary(dataContent.getBytes()));
        fileModel.setSize(dataContent.getBytes().length);
        try {
            fileModel.setMd5(MD5Util.getMD5(new ByteArrayInputStream(dataContent.getBytes())));
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("获取文件md出错");
            e.printStackTrace();
        }
        fileService.saveFile(fileModel);
        //更新数据库
        Double size = (double) (dataContent.getBytes().length / 1024);
        BigDecimal b = new BigDecimal(size);
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataSample.setDataSize(size);
        dataSample.setModifiedTime(new Timestamp(new Date().getTime()));
        dataService.updateFile(dataSample);

        // 3. 更新hash值到fabric 二次上链
        String record = fabricService.updateForModifyFile(username,srcChannelName, dataSample.hashCode()+"", String.valueOf(dataId), txId);
        log.info("更新hash值结果：" + record);
        log.info("************fabric更新文件操作记录区块链结束*****************");
        return new CommonResult<>(200, "id为：" + dataId + "的文件更新成功\r\ntxId：" + txId, null);
    }

    
    //根据上传者id获取文件列表
    @CheckToken
    @GetMapping(value = "/data/getDataListByOriginUserId")
    // TODO
    public CommonResult getDataListByOriginUserId(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
//        String token = httpServletRequest.getHeader("token");
//        Integer userId = JWT.decode(token).getClaim("id").asInt();
//        User user = userService.findUserById(userId);
//        List<DataSample> dataList = dataService.getDataListByOriginUserId(userId);
//        List<DataUserAuthorityVO> result = new ArrayList<>();
//        for (int i = 0; i < dataList.size(); i++) {
//            DataSample dataSample = dataList.get(i);
//            DataUserAuthorityVO dataUserAuthorityVO = new DataUserAuthorityVO();
//            dataUserAuthorityVO.setDataSample(dataSample);
//            dataUserAuthorityVO.setChannelName(channelService.findChannelById(dataSample.getChannelId()).getChannelName());
//            List<DataAuthority> list1 = dataAuthorityService.findDataAuthorityByUserIdAndDataId(userId,dataSample.getId());
//            Set<Integer> authorities = new HashSet<>();
//            for (int j = 0; j < list1.size(); j++) {
//                authorities.add(list1.get(j).getAuthorityKey());
//            }
//            dataUserAuthorityVO.setAuthoritySet(authorities);
//            result.add(dataUserAuthorityVO);
//        }
//        // 查询push权限
//        Map<Integer,DataUserAuthorityVO> map = new HashMap<>();
//        result.forEach(dataUserAuthorityVO -> map.put(dataUserAuthorityVO.getDataSample().getId(), dataUserAuthorityVO));
//        List<PushDataInfoDto> innerChannelPushData = channelDataAuthorityService.getInnerChannelPushData(user.getId(), user.getChannelId());
//        for (PushDataInfoDto pushDataInfoDto : innerChannelPushData) {
//            Integer dataId = pushDataInfoDto.getDataId();
//            DataUserAuthorityVO dataUserAuthorityVO = map.get(dataId);
//            if(dataUserAuthorityVO != null){
//                if(dataUserAuthorityVO.getPushChannelSet() == null){
//                    dataUserAuthorityVO.setPushChannelSet(new HashSet<>());
//                }
//                dataUserAuthorityVO.getPushChannelSet().add(new Channel(pushDataInfoDto.getChannelId(),pushDataInfoDto.getChannelName(), pushDataInfoDto.getHospitalName()));
//            }
//        }
        return new CommonResult<>(200, "获取该用户所有文件列表成功", null);
    }

}
