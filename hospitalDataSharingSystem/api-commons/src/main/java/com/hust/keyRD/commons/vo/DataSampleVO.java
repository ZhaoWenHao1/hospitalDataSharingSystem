package com.hust.keyRD.commons.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @program: system
 * @description: DataSample VO
 * @author: zwh
 * @create: 2021-03-22 11:46
 **/
@Data
public class DataSampleVO {
    private Integer id;
    private Integer channelId;
    private String channelName;
    private String hospitalName;
    private String mongoId;
    private String dataName;
    private String dataType;
    private Double dataSize;
    private Integer originUserId;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
}
