package com.hust.keyRD.commons.vo;

import com.hust.keyRD.commons.entities.DataSample;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SharedDataVO {
    private Integer shareUserId;
    private String shareUserName;//分享者的名字
    private String shareUserChannelName;//分享者所在的channel名称
    private Integer sharedUserId;
    private String sharedUserName;//被分享者的名字
    private String sharedUserChannelName;//被分享者所在的channel名称
    private DataSample dataSample;//分享的文件
    private String dataSampleChannelName;//分享的文件所在的channel
    private Integer authorityKey;
    private Integer acceptOrNot;//是否同意，同意为1，不同意或者尚未同意为0
}
