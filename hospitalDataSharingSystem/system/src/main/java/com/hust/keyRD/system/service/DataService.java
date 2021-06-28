package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.DataSample;
import java.util.List;
import java.util.Map;

public interface DataService {
    void uploadFile(DataSample dataSample);
    //获取文件列表
    List<DataSample> getDataList();
    //根据id删除文件
    Integer deleteDataById(Integer id);
    //根据id获取文件内容
    DataSample findDataById(Integer dataId);
    //更新文件
    void updateFile(DataSample dataSample);
    //获取文件列表
    List<DataSample> getDataListByOriginUserId(Integer originUserId);
    
    // 文件共享次数加一
    void sharedCountIncrease(Integer dataId);
    
    // 获得用户对用户所在channel所有文件的权限

    /**
     * 获得用户对用户所在channel所有文件的权限
     * @param userId 当前用户id
     * @param channelId  用户所在channel id
     * @return
     */
    List<UserInnerDataDto> getUserInnerDataListByUserIdAndChannelId(Integer userId, Integer channelId);


    Map<Channel, List<DataSample>> getGroupedDataList(Integer originUserId);

    Map<Integer, List<DataSample>> getDataListGroupByChannel(Integer originUserId);
}
