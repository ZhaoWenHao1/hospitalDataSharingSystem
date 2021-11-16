package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //返回所有用户
    List<User> getAllUser();
    //根据id查用户
    User findUserById(Integer id);
    //根据username查用户
    User findUserByUsername(String username);
    
    // 获取channels中的用户
    List<User> findUserByChannel(List<Integer> channels);

    //用户登录
    boolean login(User user);

    //用户注册
    void register(User user);

    /**
     * 获取所有用户 按照channel分类
     * @return
     */
    Map<Channel, List<User>> getGroupedUserList();

    /**
     * 更新用户属性
     * @param user
     */
    void updateAttributes(User user);

    List<User> getAllUsersExMe(Integer userId);
}
