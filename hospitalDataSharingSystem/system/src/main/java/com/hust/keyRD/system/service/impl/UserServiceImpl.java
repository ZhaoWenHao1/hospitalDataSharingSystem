package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.system.dao.UserDao;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private ChannelService channelService;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User findUserById(Integer id) {
        return userDao.findUserById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public boolean login(User user) {
        if(userDao.login(user) >= 1) {
            return true;
        }
        else 
            return false;
    }

    @Override
    public boolean register(User user) {
        try{
            userDao.register(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Map<Channel, List<User>> getGroupedUserList() {
        List<User> allUser = getAllUser();
        Map<Integer, List<User>> collect = allUser.stream().collect(Collectors.groupingBy(User::getChannelId));
        Map<Channel, List<User>> result = new HashMap<>();
        collect.forEach((k,v) -> {
            Channel channel = channelService.findChannelById(k);
            result.put(channel, v);
        });
        return result;
    }
}
