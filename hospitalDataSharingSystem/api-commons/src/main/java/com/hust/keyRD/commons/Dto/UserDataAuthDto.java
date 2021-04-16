package com.hust.keyRD.commons.Dto;

import lombok.Data;

import java.util.Set;

/**
 * @program: fabricPlatform
 * @description:
 * @author: 14287
 * @create: 2021/2/3 11:41
 **/
@Data
public class UserDataAuthDto {
    private Integer userId;
    private String userName;
    private String channelName;
    private String hospitalName;
    private Integer dataId;
    private String dataName;
    private String dataAuthorityList;
}
