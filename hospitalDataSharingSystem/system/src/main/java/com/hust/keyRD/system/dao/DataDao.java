package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.entities.DataSample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataDao {
    //上传文件
    void uploadFile(DataSample dataSample);
    //获取文件列表
    List<DataSample> getDataList();
    //根据文件id删除文件
    Integer deleteDataById(Integer id);
    //根据id获取文件内容
    DataSample findDataById(Integer id);
    //更新文件
    void updateFile(DataSample dataSample);
    //根据上传者id获取文件列表
    List<DataSample> getDataListByOriginUserId(Integer originUserId);

    /**
     * 获得用户对用户所在channel所有文件的权限
     * @param userId 当前用户id
     * @param channelId  用户所在channel id
     * @return
     */
    List<UserInnerDataDto> getUserInnerDataListByUserIdAndChannelId(@Param("userId") Integer userId, @Param("channelId")Integer channelId);
}
