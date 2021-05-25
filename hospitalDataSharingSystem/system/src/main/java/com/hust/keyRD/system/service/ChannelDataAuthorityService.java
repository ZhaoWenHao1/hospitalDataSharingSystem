package com.hust.keyRD.system.service;


import com.hust.keyRD.commons.Dto.PushDataInfoDto;
import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.vo.ChannelDataAuthorityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zwh
 * @email happyzhao1010@gmail.com
 * @date 2021-03-20 13:21:45
 */
public interface ChannelDataAuthorityService {

    /**
     * 获取当前用户可pull的其他域的文件list
     * @param userId 当前用户id
     * @param channelId 当前用户所在channelId
     * @return
     */
    List<DataSample> getInterChannelPullData(Integer userId, Integer channelId);

    /**
     * 对同样的权限进行计数
     * @param channelDataAuthority
     * @return
     */
    int countByChannelData(ChannelDataAuthority channelDataAuthority);

    ChannelDataAuthority findById(Integer id);

    void deleteById(Integer id);

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
    List<Integer> findPushableChannelId(Integer userId, Integer dataId);

    /**
     *
     * @param userId 拉取者的id
     * @param dataId 拉取的文件id
     * @param channelId 拉取文件id所在的channelId
     * @return
     */
    Integer checkPullAuthority(Integer userId, Integer dataId, Integer channelId);

    /**
     *
     * @param userId 上传者id
     * @param dataId 上传的文件id
     * @param channelId 上传到对应的channelid
     * @return
     */
    Integer checkPushAuthority(Integer userId, Integer dataId, Integer channelId);
    
    
    ChannelDataAuthority addPullAuthority(ChannelDataAuthority channelDataAuthority);

    ChannelDataAuthority addPushAuthority(ChannelDataAuthority channelDataAuthority);
    
    List<ChannelDataAuthorityVO> getPullAuthorityList();

    List<ChannelDataAuthorityVO> getPushAuthorityList();

    ChannelDataAuthority findByCondition(Integer userId, Integer dataId, Integer channelId, Integer type);
}

