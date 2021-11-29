package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.system.dao.ChannelDao;
import com.hust.keyRD.system.dao.UserDao;
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
    private ChannelDao channelDao;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public List<User> getAllUserAndAdmin() {
        return userDao.getAllUserAndAdmin();
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
    public List<User> findUserByChannel(List<Integer> channels) {
        return userDao.findUserByChannel(channels);
    }

    @Override
    public boolean login(User user) {
        if(userDao.login(user) >= 1) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void register(User user) {
        userDao.register(user);
    }

    @Override
    public Map<Channel, List<User>> getGroupedUserList() {
        List<User> allUser = getAllUser();
        Map<Integer, List<User>> collect = allUser.stream().collect(Collectors.groupingBy(User::getChannelId));
        Map<Channel, List<User>> result = new HashMap<>();
        collect.forEach((k,v) -> {
            Channel channel = channelDao.findChannelById(k);
            result.put(channel, v);
        });
        return result;
    }

    @Override
    public void updateAttributes(User user) {
        userDao.updateAttributes(user);
    }

    @Override
    public List<User> getAllUsersExMe(Integer userId) {
        return userDao.getAllUsersExMe(userId);
    }

    /**
     * 将用户的属性格式化
     * @param allAttribute
     * @return
     */
    @Override
    public String[] formatAttr(String allAttribute){
        String[] split = allAttribute.split(SystemConstant.SPLIT_SYMBOL);
        String[] attrs = new String[split.length];
        for (int i = 0;i < split.length;i++) {
//            String[] attrInfo = split[i].split(":");
//            String attr = attrInfo[0].trim() + SystemConstant.ATTR_CONNECTOR + attrInfo[1].trim();
            String attr = split[i].trim();
            attrs[i] = attr;
        }
        return attrs;
    }
}
