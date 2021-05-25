package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.Dto.PushDataInfoDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.vo.ChannelDataAuthorityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: system
 * @description: ChannelDataAuthority Dao
 * @author: zwh
 * @create: 2021-03-20 14:25
 **/
@Mapper
public interface ChannelDataAuthorityDao {
    
    void create(ChannelDataAuthority channelDataAuthority);
    
    void deleteById(Integer id);
    
    
    ChannelDataAuthority findById(Integer id);

    int countByChannelData(ChannelDataAuthority channelDataAuthority);

    List<ChannelDataAuthorityVO> getAuthorityListByType(Integer type);

    /**
     * 获取当前用户可pull的其他域的文件list
     * @param userId 当前用户id
     * @param channelId  当前用户所在channelId
     * @return
     */
    List<DataSample> getInterChannelPullData(@Param("userId")Integer userId, @Param("channelId") Integer channelId);


    /**
     * 获取当前用户可以push到其他域的(dataId,targetChannelSet)
     * @param userId 当前用户id
     * @param channelId 当前用户所在channel
     * @return
     */
    List<PushDataInfoDto> getInnerChannelPushData(@Param("userId")Integer userId, @Param("channelId") Integer channelId);



    /**
     * 查询用户userId可将dataId push到哪些channel
     * @param userId
     * @param dataId
     * @return
     */
    List<Integer> findPushableChannelId(@Param("userId") Integer userId, @Param("dataId") Integer dataId);
    
    /**
     *
     * @param userId 拉取者的id
     * @param dataId 拉取的文件id
     * @param channelId 拉取文件id所在的channelId
     * @return
     */
    Integer checkPullAuthority(@Param("userId")Integer userId, @Param("dataId")Integer dataId, @Param("channelId")Integer channelId);

    /**
     *
     * @param userId 上传者id
     * @param dataId 上传的文件id
     * @param channelId 上传到对应的channelid
     * @return
     */
    Integer checkPushAuthority(@Param("userId")Integer userId, @Param("dataId")Integer dataId, @Param("channelId")Integer channelId);

    ChannelDataAuthority findByCondition(@Param("userId")Integer userId, @Param("dataId")Integer dataId, @Param("channelId")Integer channelId, @Param("type")Integer type);
}
