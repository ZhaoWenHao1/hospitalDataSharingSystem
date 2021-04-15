package com.hust.keyRD.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelAuthority implements Serializable {
    private Integer id;
    private Integer channelId;
    private Integer userId;
    private Integer authorityKey;//权限字段 1代表添加权限
}
