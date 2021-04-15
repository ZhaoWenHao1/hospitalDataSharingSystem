package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserDataAuthDto;
import com.hust.keyRD.commons.vo.AllDataUserAuthorityVO;
import com.hust.keyRD.commons.vo.mapper.AllDataUserAuthorityVOMapper;
import com.hust.keyRD.system.service.DataAuthorityService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({"com.hust.keyRD.system", "com.hust.keyRD.commons"})
class DataAuthorityServiceImplTest {

    @Resource
    private DataAuthorityService dataAuthorityService;

    @Test
    void findUsersDataAuthority() {
        List<UserDataAuthDto> usersDataAuthority = dataAuthorityService.findUsersDataAuthority(1);
        Set<AllDataUserAuthorityVO> set = usersDataAuthority.stream().map(AllDataUserAuthorityVOMapper.INSTANCE::toAllDataUserAuthorityVO).collect(Collectors.toSet());

        List<AllDataUserAuthorityVO> allDataUserAuthorityVOS = dataAuthorityService.findUsersDataAuthorityVO();
        long sum = allDataUserAuthorityVOS.stream().filter(set::contains).count();
        assert sum == allDataUserAuthorityVOS.size();


    }

    @Test
    void findUsersDataAuthorityVO() {
        long start = System.currentTimeMillis();
        List<AllDataUserAuthorityVO> result = dataAuthorityService.findUsersDataAuthorityVO();
        result.forEach(System.out::println);
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start));
    }


}