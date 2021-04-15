package com.hust.keyRD.system.service;


import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.entities.ChannelAuthority;

import java.util.List;

public interface ChannelAuthorityService {
    List<String> getAddAuthorityChannels(Integer userId);
    //根据用户名和channelId查找channelAuthority
    List<ChannelAuthority> findChannelAuthorityByUserIdAndChannelId(Integer userId, Integer channelId);
    //添加通道权限
    void addChannelAuthority(ChannelAuthority channelAuthority);
    //撤销通道权限
    Integer deleteChannelAuthority(ChannelAuthority channelAuthority);
    //查找通道权限
    List<ChannelAuthority> findChannelAuthority(ChannelAuthority channelAuthority);
    // 根据管理员id和channelId查找该管理员所在channel的所有用户（不包括管理员）的channel权限
    List<UserChannelAuthDto> findUsersChannelAuthority(Integer adminId, Integer adminChannelId);
}
