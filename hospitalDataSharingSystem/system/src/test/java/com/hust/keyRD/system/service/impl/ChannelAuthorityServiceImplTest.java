package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.vo.ChannelDataAuthorityVO;
import com.hust.keyRD.commons.vo.UserInnerDataVO;
import com.hust.keyRD.system.dao.ChannelDataAuthorityDao;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChannelAuthorityServiceImplTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ChannelDataAuthorityDao channelDataAuthorityDao;

    @Test
    void getUserInnerDataListByUserIdAndChannelIdTest(){
        Map<Channel, List<User>> groupedUserList = userService.getGroupedUserList();
        groupedUserList.forEach((k,v)->{
            System.out.println(k);
            v.forEach(System.out::println);
        });
        Map<Channel, List<DataSample>> groupedDataList = dataService.getGroupedDataList();
        groupedDataList.forEach((k,v) ->{
            System.out.println(k);
            v.forEach(System.out::println);
        });
    }
    
    @Test
    void getAuthorityListByTypeTest(){
        List<ChannelDataAuthorityVO> authorityListByType = channelDataAuthorityDao.getAuthorityListByType(1);
        System.out.println(authorityListByType);
        authorityListByType = channelDataAuthorityDao.getAuthorityListByType(2);
        System.out.println(authorityListByType);
    }
    
    @Test
    void addPullAuthority(){
        ChannelDataAuthority channelDataAuthority = new ChannelDataAuthority(null,131,9,1,1);
        channelDataAuthorityDao.create(channelDataAuthority);
        System.out.println(channelDataAuthority);
    }
    
//    @Autowired
//    private ChannelAuthorityService channelAuthorityService;
//
//    @Resource
//    private DataSource dataSource;
//
////    @Bean
////    public SqlSessionFactory sqlSessionFactory() throws Exception{
////        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
////        sessionFactoryBean.setDataSource(dataSource);
////        return sessionFactoryBean.getObject();
////    }
//
////    public void setChannelAuthorityService(ChannelAuthorityService channelAuthorityService) {
////        this.channelAuthorityService = channelAuthorityService;
////    }
//
//    @Test
//    void findUsersChannelAuthority() {
//        List<UserChannelAuthDto> usersChannelAuthority = channelAuthorityService.findUsersChannelAuthority(125, 1);
//        System.out.println(usersChannelAuthority);
//        usersChannelAuthority.forEach(System.out::println);
////        Map<Integer, List<UserChannelAuthDto>> collect = usersChannelAuthority.stream().collect(Collectors.groupingBy(UserChannelAuthDto::getAuthorityKey));
//
//    }
    
    
}