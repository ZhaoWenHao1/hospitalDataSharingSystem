package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    //返回所有用户
    List<User> getAllUser();
    //根据id查用户
    User findUserById(Integer id);
    //根据username查用户
    User findUserByUsername(String username);
    //用户登录
    Integer login(User user);
    //用户注册
    void register(User user);
}
