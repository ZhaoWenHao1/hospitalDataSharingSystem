package com.hust.keyRD.system.controller;

import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.Dto.ShareResult;
import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.exception.mongoDB.MongoDBException;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.utils.AESUtil;
import com.hust.keyRD.commons.utils.MD5Util;
import com.hust.keyRD.commons.vo.DataSampleVO;
import com.hust.keyRD.commons.vo.UploadResult;
import com.hust.keyRD.commons.vo.UserInnerDataVO;
import com.hust.keyRD.commons.vo.mapper.DataSampleVOMapper;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.file.service.FileService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.*;


@Slf4j
@RestController
@Api(tags = "文件管理")
public class DataController {
    @Resource
    private DataService dataService;
    @Resource
    private UserService userService;
    @Resource
    private FileService fileService;
    @Resource
    private ChannelService channelService;

    //上传文件
    @CheckToken
    @PostMapping(value = "/data/uploadFile")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    // Transactional注解默认在抛出uncheck异常（继承自Runtime Exception或 Error ）时才会回滚 而IO SQL等异常属于check异常，所以不会回滚
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file,@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) throws Exception {
        //获取文件名
        String fileName = file.getOriginalFilename();
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        User user = userService.findUserById(originUserId);
        String encFile = AESUtil.Encrypt(file.getContentType(), SystemConstant.FILE_KEY);
        try {
            // 文件保存到mongoDB
            FileModel f = new FileModel(file.getOriginalFilename(), file.getContentType(), encFile.length(),
                    new Binary(encFile.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            fileService.saveFile(f);

            DataSample dataSample = new DataSample();
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
            dataSample.setDecryptionRules(params.get("rules"));
            dataSample.setChannelId(user.getChannelId());
            dataSample.setFrom(-1);
            dataService.uploadFile(dataSample);
            log.info("************fabric上传文件操作记录区块链开始*****************");
            UploadResult result = new UploadResult();
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

        List<DataSample> dataList = dataService.getDataList();
        List<DataSampleVO> res = new ArrayList<>();
        for (DataSample dataSample:dataList) {
            DataSampleVO dataSampleVO = DataSampleVOMapper.INSTANCE.toDataSampleVO(dataSample);
            dataSampleVO.setChannelName(channelService.findChannelById(dataSampleVO.getChannelId()).getChannelName());
            res.add(dataSampleVO);
        }
        return new CommonResult<>(200, "获取所有文件列表成功", res);
    }

    //解密文件
    @CheckToken
    @PostMapping(value = "/data/decData")
    @ResponseBody
    public CommonResult decData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest){
        Integer dataId = Integer.valueOf(params.get("dataId"));
        DataSample dataSample = dataService.findDataById(dataId);
        DataSample result = dataService.findDataById(dataId);
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        User user = userService.findUserById(originUserId);
        String attributes = user.getAttributes();
        //模拟检查属性是否满足
        if(new Random().nextFloat()>0.5){
            return new CommonResult<>(400, "属性不满足获取文件的权限，请申请属性", null);
        }
        if (result == null) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        // 2. 读取文件
        String fileContent = new String(Objects.requireNonNull(fileService.getFileById(dataSample.getMongoId())
                .map(FileModel::getContent)
                .map(Binary::getData)
                .orElse(null))
        );
        try {
            // 文件保存到mongoDB
            FileModel f = new FileModel(dataSample.getDataName(), dataSample.getDataType(), fileContent.length(),
                    new Binary(fileContent.getBytes()));
            f.setMd5(MD5Util.getMD5(new ByteArrayInputStream(fileContent.getBytes())));
            fileService.saveFile(f);
            DataSample newDataSample = new DataSample();
            newDataSample.setDataName(dataSample.getDataName());
            newDataSample.setDataSize(dataSample.getDataSize());
            newDataSample.setMongoId(f.getId());
            newDataSample.setOriginUserId(originUserId);
            newDataSample.setDataType(dataSample.getDataType());
            //初次创建时将初始时间和修改时间写成一样
            newDataSample.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newDataSample.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            newDataSample.setDecryptionRules(dataSample.getDecryptionRules());
            newDataSample.setFrom(dataSample.getId());
            dataService.uploadFile(newDataSample);
            log.info("************fabric上传文件操作记录区块链开始*****************");
            log.info("************fabric上传文件操作记录区块链结束*****************");
        } catch (Exception e) {
            return new CommonResult<>(400, e.getMessage(), null);
        }
        return new CommonResult<>(200, "文件解密成功", fileContent);
    }

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
    // TODO
    public CommonResult updateData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        Integer dataId = Integer.valueOf(params.get("dataId"));
        String dataContent = params.get("dataContent");
        DataSample dataSample = dataService.findDataById(dataId);
        if (dataSample == null) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        File old_file = new File(dataSample.getDataName());

        // 2. 修改文件
        // 更新mongoDB
        FileModel fileModel = fileService.getFileById(dataSample.getMongoId()).orElseThrow(MongoDBException::new);
        String encFile = AESUtil.Encrypt(dataContent,SystemConstant.FILE_KEY);
        fileModel.setContent(new Binary(encFile.getBytes()));
        fileModel.setSize(encFile.getBytes().length);
        try {
            fileModel.setMd5(MD5Util.getMD5(new ByteArrayInputStream(encFile.getBytes())));
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("获取文件md出错");
            e.printStackTrace();
        }
        fileService.saveFile(fileModel);
        //更新数据库
        Double size = (double) (encFile.getBytes().length / 1024);
        BigDecimal b = new BigDecimal(size);
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataSample.setDataSize(size);
        dataSample.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        dataService.updateFile(dataSample);

        // 3. 更新hash值到fabric 二次上链

        log.info("************fabric更新文件操作记录区块链结束*****************");
        return new CommonResult<>(200, "id为：" + dataId + "的文件更新成功\r\ntxId：", null);
    }

    

    //根据上传者id获取文件列表
    @CheckToken
    @GetMapping(value = "/data/getDataListByOriginUserId")
    public CommonResult getDataListByOriginUserId(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<DataSample> dataList = dataService.getDataListByOriginUserId(userId);
        List<DataSampleVO> res = new ArrayList<>();
        for (DataSample dataSample:dataList) {
            DataSampleVO dataSampleVO = DataSampleVOMapper.INSTANCE.toDataSampleVO(dataSample);
            dataSampleVO.setChannelName(channelService.findChannelById(dataSampleVO.getChannelId()).getChannelName());
            res.add(dataSampleVO);
        }
        return new CommonResult<>(200, "获取该用户所有文件列表成功", res);
    }

}
