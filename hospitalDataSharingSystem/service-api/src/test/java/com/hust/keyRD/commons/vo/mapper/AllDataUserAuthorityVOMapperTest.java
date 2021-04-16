package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.vo.AllChannelUserVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({"com.hust.keyRD.system", "com.hust.keyRD.commons"})
class AllDataUserAuthorityVOMapperTest {

    @Test
    void toAllDataUserAuthorityVO() {
        
    }
}