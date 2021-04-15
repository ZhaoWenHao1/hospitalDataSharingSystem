package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.SharedDataAuthority;

import java.util.List;

public interface SharedDataAuthorityService {
    //添加分享权限消息
    void addSharedDataAuthority(SharedDataAuthority sharedDataAuthority);
    //对请求 分享权限消息 处理结果进行更新
    void optOnSharedDataAuthority(SharedDataAuthority sharedDataAuthority);
    //获取所有请求分享信息
    List<SharedDataAuthority> receiveAllSharedDataMsg();
    //检查是否有请求分享的信息
    Integer checkSharedData(SharedDataAuthority sharedDataAuthority);
}
