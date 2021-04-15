package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.entities.ChannelAuthority;
import com.hust.keyRD.system.dao.ChannelAuthorityDao;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChannelAuthorityServiceImpl implements ChannelAuthorityService {
    @Resource
    private ChannelAuthorityDao channelAuthorityDao;

    @Override
    public List<String> getAddAuthorityChannels(Integer userId) {
        return channelAuthorityDao.getAddAuthorityChannels(userId);
    }

    @Override
    public List<ChannelAuthority> findChannelAuthorityByUserIdAndChannelId(Integer userId, Integer channelId) {
        return channelAuthorityDao.findChannelAuthorityByUserIdAndChannelId(userId, channelId);
    }

    @Override
    public void addChannelAuthority(ChannelAuthority channelAuthority) {
        channelAuthorityDao.addChannelAuthority(channelAuthority);
    }

    @Override
    public Integer deleteChannelAuthority(ChannelAuthority channelAuthority) {
        return channelAuthorityDao.deleteChannelAuthority(channelAuthority);
    }

    @Override
    public List<ChannelAuthority> findChannelAuthority(ChannelAuthority channelAuthority) {
        return channelAuthorityDao.findChannelAuthority(channelAuthority);
    }

    @Override
    public List<UserChannelAuthDto> findUsersChannelAuthority(Integer adminId, Integer adminChannelId) {
        return channelAuthorityDao.findUsersChannelAuthority(adminId, adminChannelId);
    }
}
