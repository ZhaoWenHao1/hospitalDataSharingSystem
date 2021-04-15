package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.PushDataInfoDto;
import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.exception.BadRequestException;
import com.hust.keyRD.commons.vo.ChannelDataAuthorityVO;
import com.hust.keyRD.system.dao.ChannelDataAuthorityDao;
import com.hust.keyRD.system.service.ChannelDataAuthorityService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("channelDataAuthorityService")
public class ChannelDataAuthorityServiceImpl implements ChannelDataAuthorityService {
    @Resource
    private ChannelDataAuthorityDao channelDataAuthorityDao;
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    @Resource
    private DataService dataService;

    @Override
    public ChannelDataAuthority findById(Integer id) {
        return channelDataAuthorityDao.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        channelDataAuthorityDao.deleteById(id);
    }

    @Override
    public List<DataSample> getInterChannelPullData(Integer userId, Integer channelId) {
        return channelDataAuthorityDao.getInterChannelPullData(userId, channelId);
    }

    @Override
    public List<PushDataInfoDto> getInnerChannelPushData(Integer userId, Integer channelId) {
        return channelDataAuthorityDao.getInnerChannelPushData(userId, channelId);
    }

    @Override
    public ChannelDataAuthority addPullAuthority(ChannelDataAuthority channelDataAuthority) {
        if(channelDataAuthority.getType() != 2){
            throw new BadRequestException("addPullAuthority fail, no pull type");
        }
        User pullUser = userService.findUserById(channelDataAuthority.getUserId());
        DataSample dataSample = dataService.findDataById(channelDataAuthority.getDataId());
        if(!dataSample.getChannelId().equals(channelDataAuthority.getChannelId())){
            throw new BadRequestException("addPullAuthority fail, file does not belong target channel");
        }
        if(pullUser.getChannelId().equals(dataSample.getChannelId())){
            throw new BadRequestException("addPullAuthority fail, user's channel is equal file's channel");
        }
        channelDataAuthorityDao.create(channelDataAuthority);
        return channelDataAuthority;
    }

    @Override
    public ChannelDataAuthority addPushAuthority(ChannelDataAuthority channelDataAuthority) {
        if(channelDataAuthority.getType() != 1){
            throw new BadRequestException("addPullAuthority fail, no push type");
        }
        User pullUser = userService.findUserById(channelDataAuthority.getUserId());
        DataSample dataSample = dataService.findDataById(channelDataAuthority.getDataId());
        if(!pullUser.getChannelId().equals(dataSample.getChannelId())){
            throw new BadRequestException("addPullAuthority fail, user's channel is not equal file's channel");
        }
        if(dataSample.getChannelId().equals(channelDataAuthority.getChannelId())){
            throw new BadRequestException("addPullAuthority fail, file  belongs target channel");
        }
        channelDataAuthorityDao.create(channelDataAuthority);
        return channelDataAuthority;
    }

    @Override
    public List<ChannelDataAuthorityVO> getPullAuthorityList() {

        List<ChannelDataAuthorityVO> pullAuthorityList= channelDataAuthorityDao.getAuthorityListByType(2);
        pullAuthorityList.forEach(channelDataAuthorityVO -> {
            channelDataAuthorityVO.setUserChannelName(channelService.findChannelById(channelDataAuthorityVO.getUserChannelId()).getChannelName());
            channelDataAuthorityVO.setDataChannelName(channelService.findChannelById(channelDataAuthorityVO.getDataChannelId()).getChannelName());
            channelDataAuthorityVO.setChannelName(channelService.findChannelById(channelDataAuthorityVO.getChannelId()).getChannelName());
        });
        return pullAuthorityList;
    }

    @Override
    public List<ChannelDataAuthorityVO> getPushAuthorityList() {
        List<ChannelDataAuthorityVO> pullAuthorityList= channelDataAuthorityDao.getAuthorityListByType(1);
        pullAuthorityList.forEach(channelDataAuthorityVO -> {
            channelDataAuthorityVO.setUserChannelName(channelService.findChannelById(channelDataAuthorityVO.getUserChannelId()).getChannelName());
            channelDataAuthorityVO.setDataChannelName(channelService.findChannelById(channelDataAuthorityVO.getDataChannelId()).getChannelName());
            channelDataAuthorityVO.setChannelName(channelService.findChannelById(channelDataAuthorityVO.getChannelId()).getChannelName());
        });
        return pullAuthorityList;
    }

    @Override
    public ChannelDataAuthority findByCondition(Integer userId, Integer dataId, Integer channelId, Integer type) {
        return channelDataAuthorityDao.findByCondition(userId, dataId,  channelId,  type);
    }

    @Override
    public Integer checkPullAuthority(Integer userId, Integer dataId, Integer channelId) {
        return channelDataAuthorityDao.checkPullAuthority(userId,dataId,channelId);
    }

    @Override
    public Integer checkPushAuthority(Integer userId, Integer dataId, Integer channelId) {
        return channelDataAuthorityDao.checkPushAuthority(userId,dataId,channelId);
    }
}