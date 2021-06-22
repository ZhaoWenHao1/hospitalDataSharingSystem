package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.Apply;
import com.hust.keyRD.commons.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    //返回所有用户
    List<User> getAllUser();
    //根据id查用户
    User findUserById(Integer id);
    //根据username查用户
    User findUserByUsername(String username);
    // 获取channels中的用户
    List<User> findUserByChannel(@Param("channels") List<Integer> channels);
    //用户登录
    Integer login(User user);
    //用户注册
    void register(User user);

    //更新用户属性
    void updateAttributes(User user);
    //检查userId是否拥有attribute属性
    int checkAttribute(@Param("userId")Integer userId,@Param("attributes")String attributes);
}
