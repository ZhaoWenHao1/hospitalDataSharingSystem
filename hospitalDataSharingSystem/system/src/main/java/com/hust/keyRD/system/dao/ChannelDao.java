package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.Channel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelDao {
    //获取所有channel
    List<Channel> getAllChannel();
    //根据channelID查找channel
    Channel findChannelById(Integer channelId);
    //根据channel名称查找channel
    Channel findChannelByName(String channelName);
}
