package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.system.service.DataService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DataServiceImplTest {

    @Autowired
    private DataService dataService;

    @Test
    void checkDecAttr() {
        System.out.println(dataService.checkDecAttr("Administrator and CBRC", "position:director,  subject:CBRC, subject:Bank,position:Administrator"));
    }
}