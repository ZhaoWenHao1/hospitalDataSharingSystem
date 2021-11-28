package com.hust.keyRD.system.api.v2.service;

public interface FabricService {

    /**
     * 声明新增文件及其属性
     * @param channelName 调用者所在channel
     * @param hashData 文件hash
     * @param dataId 文件id
     * @param policy 文件加密策略
     */
    void addDataAndPolicy(String channelName, String hashData, String dataId, String policy);

    /**
     * 新增用户时声明新用户属性
     * @param channelName 调用者所在channel
     * @param certificate 用户证书
     * @param userId 用户id
     * @param attrSet 用户属性集
     */
    void addUser(String channelName, String certificate, String userId, String attrSet);

    /**
     * 增加用户属性
     * @param channelName 调用者所在channel
     * @param fromUser 将fromUser的attr属性授予toUser
     * @param toUser 将fromUser的attr属性授予toUser
     * @param attr 属性
     * @param result 结果 success/failure
     */
    void addAttr(String channelName, String fromUser, String toUser, String attr, String result);

    /**
     * 将文件共享到其他channel的用户，将srcChannelName上的dataId共享给targetChannelName的userId,链码由targetChannelName的userId调用
     * @param srcChannelName 原channel
     * @param targetChannelName 目标channel
     * @param hashData 文件hash
     * @param userId 接收文件的目标channel的用户
     * @param dataId 文件id
     * @param result 结果 success/failure
     */
    void dataShare(String srcChannelName, String targetChannelName, String hashData, String userId, String dataId, String result);
}
