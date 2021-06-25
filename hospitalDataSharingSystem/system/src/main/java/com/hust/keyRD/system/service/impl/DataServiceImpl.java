package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.vo.DataSampleGroupByChannelVO;
import com.hust.keyRD.system.dao.DataDao;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
    public void sharedCountIncrease(Integer dataId) {
        DataSample dataSample = dataDao.findDataById(dataId);
        int sharedCount = dataSample.getSharedCount();
        dataSample.setSharedCount(++sharedCount);
        dataDao.updateFile(dataSample);
    }

    @Override
    public List<UserInnerDataDto> getUserInnerDataListByUserIdAndChannelId(Integer userId, Integer channelId) {
        return dataDao.getUserInnerDataListByUserIdAndChannelId(userId, channelId);
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

    @Override
    public Map<Integer, List<DataSample>> getDataListGroupByChannel() {
        List<DataSample> dataList = dataDao.getDataList();
        Map<Integer, List<DataSample>> collect = dataList.stream().collect(Collectors.groupingBy(DataSample::getChannelId));
        return collect;
    }
}
