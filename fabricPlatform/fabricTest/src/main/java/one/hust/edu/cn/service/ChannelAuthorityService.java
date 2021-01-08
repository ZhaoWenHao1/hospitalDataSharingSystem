package one.hust.edu.cn.service;


import one.hust.edu.cn.entities.ChannelAuthority;

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
}
