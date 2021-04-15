package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.system.dao.ChannelDao;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.system.service.ChannelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {
    @Resource
    ChannelDao channelDao;
    @Override
    public List<Channel> getAllChannel() {
        return channelDao.getAllChannel();
    }

    @Override
    public Channel findChannelById(Integer channelId) {
        return channelDao.findChannelById(channelId);
    }

    @Override
    public Channel findChannelByName(String channelName) {
        return channelDao.findChannelByName(channelName);
    }
}
