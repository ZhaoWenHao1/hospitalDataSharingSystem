package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.User;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;

    @Test
    void findUserByChannel() {
        List<Integer> channels = new ArrayList<>();
        channels.add(1);
        channels.add(2);
        List<User> users = userService.findUserByChannel(channels);
        users.forEach(System.out::println);
    }
}