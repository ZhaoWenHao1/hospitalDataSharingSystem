package com.hust.keyRD.system.api.v2.service.impl;

import com.hust.keyRD.system.api.v2.feign.FeignService;
import com.hust.keyRD.system.api.v2.service.FabricService;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/28 10:26
 **/
@Slf4j
@Service
public class FabricServiceImpl implements FabricService {

    @Autowired
    private FeignService feignService;


    @Override
    public void addDataAndPolicy(String channelName, String hashData, String dataId, String policy) {
        List<String> args = new ArrayList<String>() {{
            add(hashData);
            add(dataId);
            add(channelName);
            add(new Date().toString());
            add(policy);
        }};

        Response response = feignService.attrPolicy(channelName, args);
        log.info(response.body().toString());
//        feignService.attrPolicy(requester, channelName, getPeers(requester), "attrpolicy", "channel", args);

    }

    @Override
    public void addUser(String channelName, String certificate, String userId, String attrSet) {
        List<String> args = new ArrayList<String>() {{
            add(certificate);
            add(userId);
            add(channelName);
            add(new Date().toString());
            add(attrSet);
        }};

        Response response = feignService.attrUser(channelName, args);
        log.info(response.body().toString());
//        feignService.attrUser(requester, channelName, getPeers(requester), "attruser", "channel", args);
    }

    @Override
    public void addAttr(String channelName, String fromUser, String toUser, String attr, String result) {
        List<String> args = new ArrayList<String>() {{
            add(fromUser);
            add(toUser);
            add(attr);
            add(new Date().toString());
            add(result);
        }};

        Response response = feignService.addAttr(channelName, args);
        log.info(response.body().toString());
//        feignService.addAttr(requester, channelName, getPeers(requester), "addattr", "channel", args);
    }

    @Override
    public void dataShare(String srcChannelName, String targetChannelName, String hashData, String userId, String dataId, String result) {
        List<String> args = new ArrayList<String>() {{
            add(hashData);
            add(srcChannelName);
            add(userId);
            add(targetChannelName);
            add(dataId);
            add(new Date().toString());
            add(result);
        }};

        Response response = feignService.judgement(targetChannelName, args);
        log.info(response.body().toString());
//        feignService.judgement(requester, targetChannelName, getPeers(requester), "judgement", "channel", args);
    }


    private String getPeers(String requester) {
        String str1 = "peer0.org", str2 = ".example.com";
        char n = requester.charAt(3);
        return str1 + n + str2;
    }
}
