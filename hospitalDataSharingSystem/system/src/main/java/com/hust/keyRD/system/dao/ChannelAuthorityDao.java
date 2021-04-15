package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.entities.ChannelAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChannelAuthorityDao {
    //根据用户id获得已授权的通道
    List<String> getAddAuthorityChannels(Integer userId);
    //根据用户名和channelId查找channelAuthority
    List<ChannelAuthority> findChannelAuthorityByUserIdAndChannelId(@Param("userId") Integer userId,@Param("channelId") Integer channelId);
    //添加通道权限
    void addChannelAuthority(ChannelAuthority channelAuthority);
    //撤销通道权限
    Integer deleteChannelAuthority(ChannelAuthority channelAuthority);
    //查找通道权限
    List<ChannelAuthority> findChannelAuthority(ChannelAuthority channelAuthority);
    
    // 根据管理员id和channelId查找该管理员所在channel的所有用户（不包括管理员）的channel权限
    List<UserChannelAuthDto> findUsersChannelAuthority(@Param("adminId") Integer adminId, @Param("adminChannelId") Integer adminChannelId);
}
