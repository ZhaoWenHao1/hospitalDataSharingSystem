package com.hust.keyRD.system.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChannelDataAuthorityServiceTest {
    
    @Autowired
    private ChannelDataAuthorityService channelDataAuthorityService;

    @Test
    void findPushableChannelId() {
        List<Integer> pushableChannelId = channelDataAuthorityService.findPushableChannelId(142, 37);
        System.out.println(pushableChannelId);
    }
}