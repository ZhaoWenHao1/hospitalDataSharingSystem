package com.hust.keyRD.commons.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @program: system
 * @description: 域间可pull的文件信息
 * @author: zwh
 * @create: 2021-03-20 13:37
 **/
@Data
public class ChannelDataSampleVO {
    private Integer id;
    private Integer channelId;
    private String mongoId;
    private String dataName;
    private String dataType;
    private Double dataSize;
    private Integer originUserId;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
    // pull / push
    private String type;
}
