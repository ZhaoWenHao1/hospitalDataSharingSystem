package com.hust.keyRD.commons.entities;


import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author zwh
 * @email happyzhao1010@gmail.com
 * @date 2021-03-20 13:21:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDataAuthority implements Serializable {

    /**
     * channel_data_authority_id
     */
    private Integer id;

    /**
     * 发送者或拉取者id
     */
    private Integer userId;

    /**
     * 文件id
     */
    private Integer dataId;

    /**
     * channelID
     */
    private Integer channelId;

    /**
     * 权限类型：1-push，2-pull
     */
    private Integer type;


}
