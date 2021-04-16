package com.hust.keyRD.commons.vo;

import lombok.Data;

/**
 * @program: system
 * @description: ChannelDataAuthorityVO
 * @author: zwh
 * @create: 2021-03-22 15:19
 **/
@Data
public class ChannelDataAuthorityVO {
    /**
     * channel_data_authority_id
     */
    private Integer id;

    /**
     * 发送者或拉取者id
     */
    private Integer userId;
    
    private String username;
    
    private Integer userChannelId;
    
    private String userChannelName;
    
    private String userHospitalName;

    /**
     * 文件id
     */
    private Integer dataId;
    
    private String dataName;
    
    private Integer dataChannelId;
    
    private String dataChannelName;
    
    private String dataHospitalName;

    /**
     * channelID
     */
    private Integer channelId;
    
    private String channelName;
    
    private String hospitalName;

    /**
     * 权限类型：1-push，2-pull
     */
    private Integer type;
    
}
