package com.hust.keyRD.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private Integer channelId;//所在channel的id
    private Integer isAdmin;//是否为管理员，是的为1，不是的为0
    private String fabricUserId;
    private String attributes;
}
