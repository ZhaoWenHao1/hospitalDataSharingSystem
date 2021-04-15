package com.hust.keyRD.commons.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * @program: system
 * @description: 用户在channel上的权限Dto
 * @author: zwh
 * @create: 2021-01-15 10:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChannelAuthDto {
    private Integer userId;
    private String userName;
    private Integer channelId;
    private String channelName;
    private String authSet;
//    private Integer authorityKey;
//    private String channelAuthoritySet;
}
