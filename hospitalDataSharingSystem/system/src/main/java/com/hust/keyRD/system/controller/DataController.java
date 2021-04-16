package com.hust.keyRD.system.controller;

import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.entities.*;
import com.hust.keyRD.commons.exception.mongoDB.MongoDBException;
import com.hust.keyRD.commons.utils.JwtUtil;
import com.hust.keyRD.commons.utils.MD5Util;
import com.hust.keyRD.commons.vo.DataSampleVO;
import com.hust.keyRD.commons.vo.UploadResult;
import com.hust.keyRD.commons.vo.UserInnerDataVO;
import com.hust.keyRD.commons.vo.mapper.DataSampleVOMapper;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.dao.ChannelDataAuthorityDao;
import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.system.service.*;
import com.hust.keyRD.commons.vo.DataUserAuthorityVO;
import org.bson.types.Binary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@Api(tags = "文件管理")
public class DataController {
    @Resource
    private DataService dataService;
    @Resource
    private DataAuthorityService dataAuthorityService;
    @Resource
    private FabricService fabricService;
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    @Resource
    private FileService fileService;
    @Resource
    private ChannelDataAuthorityService channelDataAuthorityService;
    @Resource
    private ChannelDataAuthorityDao channelDataAuthorityDao;


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
            dataSample.setCreatedTime(new Timestamp(new Date().getTime()));
            dataSample.setModifiedTime(new Timestamp(new Date().getTime()));
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
          dataAuthorityService.addMasterDataAuthority(originUserId, dataSample.getId());
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
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<DataSample> allList = dataService.getDataList();
        List<DataAuthority> list = dataAuthorityService.findDataAuthorityByUserId(userId);//获取该用户的所有权限
        List<DataUserAuthorityVO> dataUserAuthorityVOS = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {//首先获取该用户拥有权限的文件
            DataUserAuthorityVO temp = new DataUserAuthorityVO();

            Integer dataSampleId = list.get(i).getDataSampleId();
            if (!set.contains(dataSampleId)) {
                set.add(dataSampleId);
                DataSample dataSample = dataService.findDataById(dataSampleId);
                Channel channel = channelService.findChannelById(dataSample.getChannelId());
                temp.setDataSample(dataSample);
                temp.setChannelName(channel.getChannelName());
                Set<Integer> s = new HashSet<>();
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getDataSampleId().equals(dataSampleId)) {
                        s.add(list.get(j).getAuthorityKey());
                    }
                }
                temp.setAuthoritySet(s);
                dataUserAuthorityVOS.add(temp);
            }
        }
        Set<Integer> hasDataAuthorityId = new HashSet<>();
        for (int i = 0; i < dataUserAuthorityVOS.size(); i++) {//将有文件的id取出来，在下面进行对比，找出没有权限的
            DataUserAuthorityVO dataUserAuthorityVO = dataUserAuthorityVOS.get(i);
            hasDataAuthorityId.add(dataUserAuthorityVO.getDataSample().getId());
        }
        for (int i = 0; i < allList.size(); i++) {//所有文件中没有权限的文件进行添加
            DataSample dataSample = allList.get(i);
            if(!hasDataAuthorityId.contains(dataSample.getId())){
                DataUserAuthorityVO dataUserAuthorityVO = new DataUserAuthorityVO();
                dataUserAuthorityVO.setDataSample(dataSample);
                dataUserAuthorityVO.setChannelName(channelService.findChannelById(dataSample.getChannelId()).getChannelName());
                dataUserAuthorityVO.setAuthoritySet(new HashSet<>());
                dataUserAuthorityVOS.add(dataUserAuthorityVO);
            }
        }
        Map<Channel,List<DataUserAuthorityVO>> result = new HashMap<>();
        List<Channel> channels = channelService.getAllChannel();
        for (int i = 0; i < channels.size(); i++) {//按照channel进行划分
            Channel channel = channels.get(i);
            List<DataUserAuthorityVO> dataUserAuthorityVOList = new ArrayList<>();
            for (int j = 0; j < dataUserAuthorityVOS.size(); j++) {
                DataUserAuthorityVO dataUserAuthorityVO = dataUserAuthorityVOS.get(j);
                if(channel.getId().equals(dataUserAuthorityVO.getDataSample().getChannelId())){
                    dataUserAuthorityVOList.add(dataUserAuthorityVO);
                }
            }
            result.put(channel,dataUserAuthorityVOList);
        }
        return new CommonResult<>(200, "获取该用户所有文件权限列表成功", result);
    }

    //根据文件id删除文件
    //上传文件
//    @CheckToken
//    @PostMapping(value = "/data/deleteDataById")
//    @ResponseBody
//    public CommonResult deleteDataById(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
//        Integer dataId = Integer.valueOf(params.get("dataId"));
//        // 从 http 请求头中取出 token
//        String token = httpServletRequest.getHeader("token");
//
//        Integer result = dataService.deleteDataById(dataId);
//        if (result < 1) {
//            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
//        }
//        return new CommonResult<>(200, "成功删除id为：" + dataId + "的文件,token为：" + token, null);
//    }

    //根据文件id获取文件内容
    @CheckToken
    @PostMapping(value = "/data/getData")
    @ResponseBody
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
    
    // 获取channel间数据
    // 只获取当前用户可pull的其他channel的数据
    @ApiOperation("获取当前用户可pull的其他channel的文件")
    @GetMapping("/data/getInterChannelData")
    public CommonResult<List<DataSampleVO>> getInterChannelData(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JwtUtil.parseJWT(token).get("id", Integer.class);
        User user = userService.findUserById(userId);
        List<DataSample> interChannelPullDataList = channelDataAuthorityService.getInterChannelPullData(user.getId(), user.getChannelId());
        List<DataSampleVO> dataSampleVOList = interChannelPullDataList.parallelStream()
                .map(DataSampleVOMapper.INSTANCE::toDataSampleVO)
                .peek(dataSampleVO -> {
                   Channel channel = channelService.findChannelById(dataSampleVO.getChannelId());
                    dataSampleVO.setHospitalName(channel.getHospitalName());
                    dataSampleVO.setChannelName(channel.getChannelName());
//                    return dataSampleVO;
                })
                .collect(Collectors.toList());

        return new CommonResult<>(200,"success", dataSampleVOList);
    }

    // 获取当前channel的文件
    // 除对每个文件增删改查外，还能进行文件push到其他channel的权限
    @ApiOperation("获取当前channel的文件 除对每个文件增删改查外，还能进行文件push到其他channel的权限")
    @GetMapping("/data/getCurrentChannelData")
    public CommonResult<List<UserInnerDataVO>> getCurrentChannelData(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<UserInnerDataVO> userInnerDataVOList = dataService.getCurrentChannelData(userId);
        userInnerDataVOList.parallelStream().forEach(userInnerDataVO -> {
            userInnerDataVO.setChannelName(channelService.findChannelById(userInnerDataVO.getChannelId()).getChannelName());
        });
        return new CommonResult<>(200, "success", userInnerDataVOList);
    }
//
    // 当前用户将channelId的文件dataId  pull到用户所在的channel
    @ApiOperation("当前用户将channelId的文件dataId  pull到用户所在的channel")
    @PostMapping("/data/pullData")
    public CommonResult pullData(@RequestBody Map<String, String> params,HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        // 所pull的文件Id
        Integer dataId = Integer.valueOf(params.get("dataId"));
        // 文件所在的channelId
        Integer channelId = Integer.valueOf(params.get("channelId"));
        User user = userService.findUserById(userId);
        Integer checkAuthorityCount = channelDataAuthorityService.checkPullAuthority(userId,dataId,channelId);
        if(checkAuthorityCount>=1){
            Optional<FileModel> fileModel = fileService.getFileById(dataService.findDataById(dataId).getMongoId());
            FileModel newFileModel = fileService.copyFile(fileModel.get());//获取新文件,已经保存至mangodb中
            //将新文件保存至数据库，文件channelId为该用户所在的channelId
            DataSample newDataSample = new DataSample();
            newDataSample.setChannelId(user.getChannelId());
            newDataSample.setMongoId(newFileModel.getId());
            newDataSample.setDataName(newFileModel.getName());
            newDataSample.setDataType(newFileModel.getName().substring(newFileModel.getName().lastIndexOf(".")) + "文件");
            //初次创建时将初始时间和修改时间写成一样
            newDataSample.setCreatedTime(new Timestamp(new Date().getTime()));
            newDataSample.setModifiedTime(new Timestamp(new Date().getTime()));
            //文件大小以KB作为单位
            // 首先先将.getSize()获取的Long转为String 单位为B
            Double size = Double.parseDouble(String.valueOf(newFileModel.getSize()));
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 此时size就是保留两位小数的浮点数
            newDataSample.setDataSize(size);
            newDataSample.setOriginUserId(userId);
            dataService.uploadFile(newDataSample);//上传至数据库
            //写入上传者权限
            dataAuthorityService.addMasterDataAuthority(userId, newDataSample.getId());
            //TODO fabric操作

            return new CommonResult<>(200, "success", newDataSample);
        }else {
            return new CommonResult<>(400, "您没有拉取该文件的权限", null);
        }

    }
//
    // 当前用户将用户所在channel的dataId push到channelId上
    @ApiOperation("当前用户将用户所在channel的dataId push到channelId上")
    @PostMapping("/data/pushData")
    public CommonResult pushData(@RequestBody Map<String, String> params,HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        // 所pull的文件Id
        Integer dataId = Integer.valueOf(params.get("dataId"));
        // 目标channelId 要发送到该channel上
        Integer channelId = Integer.valueOf(params.get("channelId"));
        User user = userService.findUserById(userId);
        Integer checkAuthorityCount = channelDataAuthorityService.checkPushAuthority(userId,dataId,channelId);
        if(checkAuthorityCount>=1){
            Optional<FileModel> fileModel = fileService.getFileById(dataService.findDataById(dataId).getMongoId());
            FileModel newFileModel = fileService.copyFile(fileModel.get());//获取新文件,已经保存至mangodb中
            //将新文件保存至数据库，文件channelId为对应channelId
            DataSample newDataSample = new DataSample();
            newDataSample.setChannelId(channelId);
            newDataSample.setMongoId(newFileModel.getId());
            newDataSample.setDataName(newFileModel.getName());
            newDataSample.setDataType(newFileModel.getName().substring(newFileModel.getName().lastIndexOf(".")) + "文件");
            //初次创建时将初始时间和修改时间写成一样
            newDataSample.setCreatedTime(new Timestamp(new Date().getTime()));
            newDataSample.setModifiedTime(new Timestamp(new Date().getTime()));
            //文件大小以KB作为单位
            // 首先先将.getSize()获取的Long转为String 单位为B
            Double size = Double.parseDouble(String.valueOf(newFileModel.getSize()));
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 此时size就是保留两位小数的浮点数
            newDataSample.setDataSize(size);
            newDataSample.setOriginUserId(userId);
            dataService.uploadFile(newDataSample);//上传至数据库
            //不写入上传者权限！
            //TODO fabric操作

            return new CommonResult<>(200, "success", newDataSample);
        }else {
            return new CommonResult<>(400, "您没有上传该文件到对于通道的权限", null);
        }
    }
    
    // 中心链跨链权限管理 分为push权限和pull权限

    // 获取data列表
    @ApiOperation("获取以channel分类的data列表")
    @GetMapping("/data/getGroupedDataList")
    public CommonResult<Map<Channel, List<DataSample>>> getGroupedDataList(){
        Map<Channel, List<DataSample>> groupedDataList = dataService.getGroupedDataList();
        return new CommonResult<>(200, "success", groupedDataList);
    }


    //根据文件id对文件内容进行更新
    @CheckToken
    @PostMapping(value = "/data/updateData")
    @ResponseBody
    @Transactional
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
    public CommonResult getDataListByOriginUserId(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<DataSample> list = dataService.getDataListByOriginUserId(userId);
        List<DataUserAuthorityVO> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DataSample dataSample = list.get(i);
            DataUserAuthorityVO dataUserAuthorityVO = new DataUserAuthorityVO();
            dataUserAuthorityVO.setDataSample(dataSample);
            dataUserAuthorityVO.setChannelName(channelService.findChannelById(dataSample.getChannelId()).getChannelName());
            List<DataAuthority> list1 = dataAuthorityService.findDataAuthorityByDataId(dataSample.getId());
            Set<Integer> authorities = new HashSet<>();
            for (int j = 0; j < list1.size(); j++) {
                authorities.add(list1.get(j).getAuthorityKey());
            }
            dataUserAuthorityVO.setAuthoritySet(authorities);
            result.add(dataUserAuthorityVO);
        }
        return new CommonResult<>(200, "获取该用户所有文件列表成功", result);
    }
    
    
}
