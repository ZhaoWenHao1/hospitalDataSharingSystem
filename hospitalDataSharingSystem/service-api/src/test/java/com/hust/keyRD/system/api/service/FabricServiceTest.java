package com.hust.keyRD.system.api.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients("com.hust.keyRD.system.api.feign")
class FabricServiceTest {
    

    @Test
    void addEncryptionPolicy() {
    }

    @Test
    void addUser() {
    }

    @Test
    void applyForAttribute() {
    }

    @Test
    void crossChannelJudgement() {
    }
}