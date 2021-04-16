package com.hust.keyRD.system.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.keyRD.commons.Dto.ShareResult;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.system.api.service.FabricService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients("com.hust.keyRD.system.api.feign")
class FabricServiceImplTest {
    
    @Autowired
    private FabricService fabricService;
    
    @Autowired
    private FabricServiceImpl fabricServiceImpl;

    @Test
    void traceBackward() {
    }

    @Test
    void testTraceBackward() {
        String fileId = "file9";
        String txId = "28df397b16ec36df92a23cd179ff5a10808bae543c776e3f6304c9db368724ba";
        Record record = fabricService.traceBackward("org5_user","channel2", "data3");
        System.out.println(record);
//        String response = "{\"next_tx\": \"28df397b16ec36df92a23cd179ff5a10808bae543c776e3f6304c9db368724ba\", \"tx_info\":\n" +
//                "\"{\\\"data_id\\\":\\\"file1\\\",\\\"dst_chain\\\":\\\"channel1\\\",\\\"hash_data\\\":\\\"456\\\",\\\"last_tx_id\\\":\\\"cbcf5226f0aaf8754859b6b22fa8a14116bc84191ab8d720011abaa6ea846a47\\\",\\\"src_chain\\\":\\\"channel2\\\",\\\"this_tx_id\\\":\\\"28df397b16ec36df92a23cd179ff5a10808bae543c776e3f6304c9db368724ba\\\",\\\"type_tx\\\":\\\"delete\\\",\\\"user\\\":\\\"userC\\\"}\"}";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            JsonNode root = mapper.readTree(response);
//            JsonNode txInfo = root.path("tx_info");
//            mapper.readValue(root.path("tx_info").toString(), Record.class);
//        } catch (IOException e) {
////            log.error("溯源失败,fileId:{},txId:{}, response:{}", fileId, txId, response);
//            throw new FabricException("溯源失败: fileId:" + fileId + ", txId: " + txId);
//        }
    }
    
    @Test
    void traceBackwardAll1(){
        List<Record> records = fabricService.traceBackwardAll("org5_user", "channel2", "data3");
        records.forEach(System.out::println);
    }

    @Test
    void grantUserPermission2Add() {
        Boolean aBoolean = fabricService.grantUserPermission2Add("org4_admin","channel2", "role1", "org4_admin");
        System.out.println(aBoolean);
    }

    @Test
    void revokeUserPermission2Add() {
        Boolean aBoolean = fabricService.revokeUserPermission2Add("org4_admin", "channel2", "role1", "org4_admin");
        System.out.println(aBoolean);
    }

    // 撤销用户对应文件权限
    @Test
    void revokeUserPermissionOnFile() {
        Boolean aBoolean = fabricService.revokeUserPermissionOnFile("org4_admin", "channel2", "data3", "modify", "role1", "org5_user");
        System.out.println(aBoolean);
    }

    @Test
    void getPolicy() {
        String ans = fabricService.getPolicy("org4_admin","channel2","channel1", "add");
        System.out.println(ans);
    }
    
    // 上传文件 一二次上链
    @Test
    void uploadFile(){
        String tx = fabricService.applyForCreateFile("org5_user", "channel2","hash","data3");
        System.out.println(tx);
        tx = fabricService.updateForCreateFile("org5_user", "channel2","hash","data3",tx);
        System.out.println(tx);
    }
    
    // 授予文件read权限
    @Test
    void grantReadPermission(){
        Boolean aBoolean = fabricService.grantUserPermissionOnFileInnerChannel("org4_admin", "data3", "channel2", "read", "role1", "org5_user");
        System.out.println(aBoolean);
        // String requester, String channelName,  String obj, String opt
        String policy = fabricService.getPolicy("org5_user", "channel2", "data3", "read");
        System.out.println(policy);
    }
    
    // 读取文件
    @Test
    void readFile(){
        String tx = fabricService.applyForReadFile("org5_user", "channel2","hash","data3");
        System.out.println(tx);
        tx = fabricService.updateForReadFile("org5_user", "channel2","hash","data3",tx);
        System.out.println(tx);
    }

    // 授予文件修改权限
    @Test
    void grantModifyPermission(){
        Boolean aBoolean = fabricService.grantUserPermissionOnFileInnerChannel("org4_admin", "data3", "channel2", "modify", "role1", "org5_user");
        System.out.println(aBoolean);
        // String requester, String channelName,  String obj, String opt
        String policy = fabricService.getPolicy("org5_user", "channel2", "data3", "modify");
        System.out.println(policy);
    }

    // 修改文件
    @Test
    void modifyFile(){
        String tx = fabricService.applyForModifyFile("org5_user", "channel2","hash","data3");
        System.out.println(tx);
        tx = fabricService.updateForModifyFile("org5_user", "channel2","hash","data3",tx);
        System.out.println(tx);
    }

    @Test
    void grantUserPermissionOnFile() {
//        Boolean aBoolean = fabricService.grantUserPermissionOnFile("file333", "channel1","read", "role1", Collections.singletonList("rick"));
//        System.out.println(aBoolean);
    }

    @Test
    void applyForReadFile() {
//        String txId = fabricService.applyForReadFile("", "channel1","userD", "channel1", "218");
//        System.out.println(txId);
    }

    @Test
    void traceBackwardAll() {
//        List<Record> recordList = fabricService.traceBackwardAll("16", "channel1");
//        recordList.forEach(System.out::println);
    }

    @Test
    void testTraceBackwardAll() {
        String txId = "e7be475651db57661993abb91e1f406ef12033e1e7e91e2538a68d632afd7fff";
        List<Record> recordList = fabricService.traceBackwardAll("16", "channel1", txId);
        recordList.forEach(System.out::println);
    }

    @Test
    void applyForCreateFile() {
        String txId = fabricService.applyForCreateFile("org5_user", "channel2", "hashdata","data2");
        System.out.println(txId);
    }


    @Test
    void getCentrePolicy() {
        String data1 = fabricService.getCentrePolicy("data1");
        System.out.println(data1);
    }

    @Test
    void grantInnerChannelPull() {
        Boolean res = fabricService.grantInnerChannelPull("data2", "role1", "org2_user");
        System.out.println(res);
        String response = fabricService.getCentrePolicy("data2");
        System.out.println(response);
    }

    @Test
    void grantInnerChannelPush() {
        Boolean res = fabricService.grantInnerChannelPush("data3", "role1", "org3_user");
        System.out.println(res);
        String response = fabricService.getCentrePolicy("data3");
        System.out.println(response);
    }

    @Test
    void revokeInnerChannelPull() {
        String response = fabricService.getCentrePolicy("data2");
        System.out.println(response);
        Boolean res = fabricService.revokeInnerChannelPull("data2", "role1", "org2_user");
        System.out.println(res);
        response = fabricService.getCentrePolicy("data2");
        System.out.println(response);
    }

    @Test
    void revokeInnerChannelPush() {
        String response = fabricService.getCentrePolicy("data3");
        System.out.println(response);
        Boolean res = fabricService.revokeInnerChannelPush("data3", "role1", "org2_user");
        System.out.println(res);
        response = fabricService.getCentrePolicy("data3");
        System.out.println(response);
    }

    @Test
    void pushData() {
        ShareResult shareResult = fabricService.pushData("org2_user", "data2", "hash", "channel1", "channel2","data2_copy");

        System.out.println(shareResult);
    }

    @Test
    void pullData() {
        // 拉取文件
        ShareResult shareResult = fabricService.pullData("org3_user", "data03", "hash", "channel1", "channel2","data03_copy");
        System.out.println(shareResult);
    }

    @Test
    void traceForwardCrossChain() {
        Record record = fabricService.traceForwardCrossChain("org5_user", "channel2", "3c6a09b1b965c8ad0215dc1a9113b05213062e895c5070e654341f88d6001041", "channel1");
        System.out.println(record);
    }
}