package com.hust.keyRD.system.api.service;

public interface FabricService {


    /**
     * 文件加密策略上链
     *
     * @param fileId
     * @param dataHash
     * @param policy
     * @param channelName 文件所在channel
     * @return
     */
    // uncheck
    boolean addEncryptionPolicy(String fileId, String dataHash, String policy, String channelName);

    /**
     * 新增用户
     *
     * @param username 用户名
     * @param attrs    用户拥有属性
     * @return
     */
    boolean addUser(String username, String attrs);

    /**
     * 申请权限
     *
     * @param applierName    申请者
     * @param targetUsername 被申请者
     * @param attr           申请的属性
     * @return
     */
    boolean applyForAttribute(String applierName, String targetUsername, String attr);

    /**
     * 判断文件是否有解密属性
     *
     * @param username 解密用户
     * @param fileId   文件id
     * @param dataHash 文件hashSAsxaAA
     * @return
     */
    // uncheck
    String crossChannelJudgement(String username, String fileId, String dataHash);

}
