package com.hust.keyRD.commons.Dto;

import lombok.Data;

/**
 * @program: system
 * @description: pushData的相关信息 由数据库查询而来
 * @author: zwh
 * @create: 2021-03-20 19:07
 **/
@Data
public class PushDataInfoDto {
    private Integer dataId;
    private Integer channelId;
    private String channelName;
}
