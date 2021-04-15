package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.Channel;

import java.util.List;

public interface ChannelService {
    //获取所有channel
    List<Channel> getAllChannel();
    //根据channelID查找channel
    Channel findChannelById(Integer channelId);
    //根据channel名称获取channel
    Channel findChannelByName(String channelName);

}
