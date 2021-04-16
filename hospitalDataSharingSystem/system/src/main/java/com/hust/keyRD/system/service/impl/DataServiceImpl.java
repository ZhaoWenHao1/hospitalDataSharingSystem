package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.PushDataInfoDto;
import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.vo.UserInnerDataVO;
import com.hust.keyRD.commons.vo.mapper.UserInnerDataVOMapper;
import com.hust.keyRD.system.dao.DataDao;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.system.service.ChannelDataAuthorityService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService
{
    @Resource
    private DataDao dataDao;
    @Resource
    private UserService userService;
    @Resource
    private DataService dataService;
    @Resource
    private ChannelDataAuthorityService channelDataAuthorityService;
    @Resource
    private ChannelService channelService;

    @Override
    public void uploadFile(DataSample dataSample) {
        dataDao.uploadFile(dataSample);
    }

    @Override
    public List<DataSample> getDataList() {
        return dataDao.getDataList();
    }

    @Override
    public Integer deleteDataById(Integer id) {
        return dataDao.deleteDataById(id);
    }

    @Override
    public DataSample findDataById(Integer dataId) {
        return dataDao.findDataById(dataId);
    }

    @Override
    public void updateFile(DataSample dataSample) {
        dataDao.updateFile(dataSample);
    }

    @Override
    public List<DataSample> getDataListByOriginUserId(Integer originUserId) {
        return dataDao.getDataListByOriginUserId(originUserId);
    }

    @Override
    public List<UserInnerDataDto> getUserInnerDataListByUserIdAndChannelId(Integer userId, Integer channelId) {
        return dataDao.getUserInnerDataListByUserIdAndChannelId(userId, channelId);
    }

    @Override
    public List<UserInnerDataVO> getCurrentChannelData(Integer userId) {
        User user = userService.findUserById(userId);
        List<UserInnerDataDto> userInnerDataList = dataService.getUserInnerDataListByUserIdAndChannelId(user.getId(), user.getChannelId());
        List<UserInnerDataVO> userInnerDataVOList = userInnerDataList.stream().map(UserInnerDataVOMapper.INSTANCE::toUserInnerDataVO).collect(Collectors.toList());
        Map<Integer, UserInnerDataVO> map = new HashMap<Integer, UserInnerDataVO>();
        userInnerDataVOList.forEach(userInnerDataVO -> map.put(userInnerDataVO.getId(), userInnerDataVO));
        List<PushDataInfoDto> innerChannelPushData = channelDataAuthorityService.getInnerChannelPushData(user.getId(), user.getChannelId());
        innerChannelPushData.forEach(pushDataInfoDto -> {
            Integer dataId = pushDataInfoDto.getDataId();
            UserInnerDataVO userInnerDataVO = map.get(dataId);
            if(userInnerDataVO != null){
                if(userInnerDataVO.getPushChannelSet() == null){
                    userInnerDataVO.setPushChannelSet(new HashSet<>());
                }
                userInnerDataVO.getPushChannelSet().add(new Channel(pushDataInfoDto.getChannelId(), pushDataInfoDto.getChannelName(), pushDataInfoDto.getHospitalName()));
            }
        });
        return userInnerDataVOList;
    }



    @Override
    public Map<Channel, List<DataSample>> getGroupedDataList() {
        List<DataSample> allData = getDataList();
        Map<Integer, List<DataSample>> collect = allData.stream().collect(Collectors.groupingBy(DataSample::getChannelId));
        Map<Channel, List<DataSample>> result = new HashMap<>();
        collect.forEach((k,v) ->{
            Channel channel = channelService.findChannelById(k);
            result.put(channel, v);
        });
        return result;
    }
}
