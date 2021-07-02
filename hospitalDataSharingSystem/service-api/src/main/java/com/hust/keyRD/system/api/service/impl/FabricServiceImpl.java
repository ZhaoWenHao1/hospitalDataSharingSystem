package com.hust.keyRD.system.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.system.api.feign.FabricFeignService;
import com.hust.keyRD.system.api.service.FabricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FabricServiceImpl implements FabricService {

    @Autowired
    private FabricFeignService fabricFeignService;


    @Override
    public boolean addEncryptionPolicy(String fileId, String dataHash, String policy, String channelName) {
        String requester = "org2_admin";
        String peers = "peer0.org2.example.com";
        String fcn = "attrPolicy";
        String ccName = "attr";
        List<String> args = new ArrayList<>();
        args.add(dataHash);
        args.add(fileId);
        args.add(channelName);
//        args.add(policy);
        if (channelName.equals("channel1")) {
            requester = "org2_admin";
            peers = "peer0.org2.example.com";
        }
        String response = fabricFeignService.addEncryptionPolicy(requester, channelName, peers, ccName, fcn, policy, args).body().toString();
        if (response != null || response.contains("success") || response.contains("already exists")) {
            return true;
        } else {
            throw new FabricException("文件加密策略上链失败");
//            return false;
        }
    }

    @Override
    public boolean addUser(String username, String attrs) {
        String requester = "org2_admin";
        String peers = "peer0.org2.example.com";
        String fcn = "attruser";
        String ccName = "attr";
        String channelName = "channel1";
        List<String> args = new ArrayList<>();
        args.add("certificate");
        args.add(username);
        args.add(channelName);
        args.add(attrs);
        String response = fabricFeignService.addUser(requester, channelName, peers, ccName, fcn, args).body().toString();
        if (response.contains("attr_array")) {
            return true;
        } else {
            throw new FabricException("新增用户出错：" + response);
        }
    }

    @Override
    public boolean applyForAttribute(String applierName, String targetUsername, String attr) {
        String requester = "org2_admin";
        String peers = "peer0.org2.example.com";
        String fcn = "addattr";
        String ccName = "attr";
        String channelName = "channel1";
        List<String> args = new ArrayList<>();
        args.add(targetUsername);
        args.add(applierName);
        args.add(attr);
        String response = fabricFeignService.applyForAttribute(requester, channelName, peers, ccName, fcn, args).body().toString();
        if(response.startsWith("{")){
            return true;
        }
        else {
            throw new FabricException("申请权限失败");
        }
    }

    @Override
    public String crossChannelJudgement(String username, String fileId, String dataHash) {
        String requester = "org2_admin";
        String peers = "peer0.org2.example.com";
        String fcn = "CrossChannelJudgement";
        String ccName = "audit";
        String channelName = "channel1";
        List<String> args = new ArrayList<>();
        args.add(dataHash);
        args.add(channelName);
        args.add(username);
        args.add(channelName);
        args.add(fileId);
        String response = fabricFeignService.crossChannelJudgement(requester, channelName, peers, ccName, fcn, args).body().toString();
        if (response.contains("false") || !response.contains("tx_id")) {
            return "";
        } else {
            // 获取上链事务id
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            try {
                root = mapper.readTree(response);
            } catch (IOException e) {
                return "";
            }
            return root.path("tx_id").asText();
        }

    }


}
