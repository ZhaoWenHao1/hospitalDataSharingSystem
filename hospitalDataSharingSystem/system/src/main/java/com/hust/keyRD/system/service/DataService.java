package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.vo.UserInnerDataVO;

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
    
    // 获得用户对用户所在channel所有文件的权限

    /**
     * 获得用户对用户所在channel所有文件的权限
     * @param userId 当前用户id
     * @param channelId  用户所在channel id
     * @return
     */
    List<UserInnerDataDto> getUserInnerDataListByUserIdAndChannelId(Integer userId, Integer channelId);

    /**
     * 获取当前channel的文件 除对每个文件增删改查外，还能进行文件push到其他channel的权限
     * @param userId 当前用户id
     * @return
     */
    List<UserInnerDataVO> getCurrentChannelData(Integer userId);



    Map<Channel, List<DataSample>> getGroupedDataList();
}
