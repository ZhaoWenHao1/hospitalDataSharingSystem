package com.hust.keyRD.system.api.service;

import com.hust.keyRD.common.redis.annotation.RedisCache;
import com.hust.keyRD.commons.Dto.ShareResult;
import com.hust.keyRD.commons.entities.Record;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FabricService {

    /**
     * 调用链码  这里是指使用中心链上org1来进行权限管理
     *
     * @param requester   调用者
     * @param peers       peer list
     * @param channelName channel名
     * @param ccName      ccName
     * @param fcn         调用函数
     * @param args        链码调用参数
     * @return 结果
     */
    String invokeChaincode(String requester, String peers, String channelName, String ccName,
                           String fcn, List<String> args);

    
    /**
     * 域内增加policy
     *
     * @param requester   链码调用者
     * @param channelName 调用者所在channel
     * @param peers       调用peers
     * @param obj         channelName 或 fileName
     * @param permission  权限
     * @param role        角色
     * @param users       用户list
     * @return
     */
    Boolean addPolicyInnerChannel(String requester, String channelName, String peers, String obj, String permission, String role, List<String> users);

    /**
     * 域内 更新 policy 即更新state <obj+permission, Map<String role, String[] users>的value
     *
     * @param obj        channelName 或 fileId
     * @param permission 权限
     * @param func       addrole, deleterole, adduser, deleteuser
     * @param role       角色
     * @param users      用户list
     * @return success/fail
     */
    Boolean updatePolicyInnerChannel(String requester, String channelName, String obj, String permission, String func, String role, List<String> users);


    /**
     * 授予 某角色 某些用户 某个channel的（文件）add权限
     *
     * @param dstChannelName 目标channel  授予该channel的增加文件的权限
     * @param role           角色
     * @param username       用户名
     * @return success / throw FabricException
     */
    Boolean grantUserPermission2Add(String requester, String dstChannelName, String role, String username);


    /**
     * 域内 授予 某角色 某些用户 某个文件的查看 修改 删除 权限
     *
     * @param requester       调用者
     * @param fileId          文件id
     * @param fileChannelName 文件所在channel
     * @param permission      权限
     * @param role            角色
     * @param username        授权用户名
     * @return
     */
    // TODO: check
    Boolean grantUserPermissionOnFileInnerChannel(String requester, String fileId, String fileChannelName, String permission, String role, String username);
    
    
    /**
     * 撤销用户对文件的相关权限
     * @param requester 调用者
     * @param channelName 调用者所在的channel
     * @param fileId 文件id
     * @param permission 权限
     * @param role 角色
     * @param username 用户名
     * @return
     */
    Boolean revokeUserPermissionOnFile(String requester, String channelName,String fileId, String permission, String role, String username);


    /**
     * 撤销 某角色 某些用户 当前channel的（文件）add权限
     *
     * @param requester   调用者
     * @param channelName 调用者所在channel
     * @param role        角色
     * @param username    要撤销权限的用户名（数据库名）
     * @return
     */
    Boolean revokeUserPermission2Add(String requester, String channelName, String role, String username);


    /**
     * 跨链请求权限 操作链第一次上链
     *
     * @param peers       peer list
     * @param channelName channel名
     * @param ccName      ccName
     * @param fcn         调用函数
     * @param args        链码调用参数
     * @return 交易 tx_id
     */
    // TODO: check
    String crossAccess(String requester, String listenPeers, String peers,  String channelName, String ccName,
                       String fcn, List<String> args);
    

    /**
     * 申请域内创建文件权限  第一次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @return
     */
    String applyForCreateFile(String requester, String channelName,String fileHash, String fileId);

    /**
     * 申请域内读取文件权限  第一次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @return
     */
    String applyForReadFile(String requester, String channelName,String fileHash, String fileId);

    /**
     * 申请域内修改文件权限  第一次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @return
     */
    String applyForModifyFile(String requester, String channelName,String fileHash, String fileId);

    /**
     * 申请域内下载文件权限  第一次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @return
     */
    String applyForDownloadFile(String requester, String channelName,String fileHash, String fileId);


    /**
     * 创建文件时 更新链上文件hash值  第二次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @param txId 第一次上链事务号
     * @return
     */
    String updateForCreateFile(String requester, String channelName,String fileHash, String fileId,String txId);

    /**
     * 查看文件时 更新链上文件hash值  第二次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @param txId 第一次上链事务号
     * @return
     */
    String updateForReadFile(String requester, String channelName,String fileHash, String fileId,String txId);
    

    /**
     * 修改文件时 更新链上文件hash值  第二次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @param txId 第一次上链事务号
     * @return
     */
    String updateForModifyFile(String requester, String channelName,String fileHash, String fileId,String txId);


    /**
     * 下载文件时 更新链上文件hash值  第二次上链
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @param txId 第一次上链事务号
     * @return
     */
    String updateForDownloadFile(String requester, String channelName,String fileHash, String fileId,String txId);

    

    /**
     * 域内第二次上链，更新链上数据hash值，实现与链下数据的同步
     * @param requester 调用者
     * @param channelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @param typeTx 操作类型 add/read/modify...
     * @param txId 第一次上链事务号
     * @return
     */
//    @CacheEvict(cacheNames = "traceAll", key = "#fileId")
    String dataSyncRecord(String requester, String channelName,String fileHash, String fileId,String typeTx, String txId);

    /**
     * 共享文件后的域内第二次上链，由于跨链共享后二次上链的fcn不同，所以需要指定fcn
     * @param requester 调用者
     * @param requesterChannelName 调用者所在channel
     * @param fileHash 文件hash
     * @param fileId 文件id
     * @param dataChannelName 文件所在channel
     * @param typeTx 操作类型 add/read/modify...
     * @param txId 第一次上链事务号
     * @param fcn  指定fcn   
     * @return
     */
    String dataSyncRecordAfterShare(String requester, String requesterChannelName,String fileHash, String fileId,String dataChannelName, String typeTx, String txId, String fcn);

    
    /**
     * 溯源 查找fileId文件最新的交易记录
     * @param requester 
     * @param channelName
     * @param fileId
     * @return
     */
    Record traceBackward(String requester, String channelName, String fileId);
    
    /**
     * 溯源 查找fileId文件txId交易的上一次交易记录  继续溯源的话使用Record.thisTxId和原fileId作为参数 将进行下一次溯源
     * @param requester
     * @param channelName
     * @param fileId
     * @param txId
     * @return
     */
//    @Cacheable(key = "#fileId + '#' + #txId ", cacheNames = "traceBackward")
//    @RedisCache(key = "trace", fieldKey = "#fileId + '#' + #txId", expired = 3600 * 24 * 7)
    Record traceBackward(String requester, String channelName, String fileId, String txId);

    /**
     * 溯源 查找fileId文件全部的交易记录
     *
     * @param fileId      文件id
     * @param channelName 文件所在channel  授予该channel文件的权限
     * @return
     */
    // TODO: check
//    @Cacheable(key = "#fileId", cacheNames = "traceAll")
    List<Record> traceBackwardAll(String requester, String channelName, String fileId);

    /**
     * 溯源 查找fileId文件txId交易的上面的全部交易记录
     *
     * @param fileId          文件id
     * @param channelName 文件所在channel  授予该channel文件的权限
     * @param txId            交易id
     * @return 上一次record实例
     */
    // TODO: check
    List<Record> traceBackwardAll(String requester, String channelName, String fileId, String txId);


    /**
     * 查询权限 如channel1上add的权限  由于权限管理由中心链完成，所以在本例中使用中心链上的org1来查询
     *
     * @param channelName channel name
     * @param opt         操作名 如 add
     * @return policy实体
     */

    String getPolicy(String requester, String channelName, String obj, String opt);
    
    /**
     * 查询权限 channel1上文件的read或modify权限  由于权限管理由中心链完成，所以在本例中使用中心链上的org1来查询
     *
     * @param fileId      要查询的fileId
     * @param channelName channelName
     * @param opt         操作 read / modify
     * @return
     */
    // TODO: check
    String getPolicy(String fileId, String channelName, String opt);


    /**
     * 获取中心链上记录的有关文件share的权限
     * @param fileId
     * @return
     */
    String getCentrePolicy(String fileId);

    /**
     * 根据交易号查询结构体信息
     *
     * @param txId  交易号（申请权限的交易号）
     * @param order 第几次上链 1/2
     * @return 结构体JSON
     */
    // TODO: check
    String queryAuthority(String txId, String order);

    String argsTest(String peers, String channelName, String ccName,
                    String fcn, List<String> args);


    /**
     * 授予域间pull权限
     * @param fileId
     * @param role
     * @param username
     * @return
     */
    Boolean grantInnerChannelPull(String fileId, String role,String username);

    /**
     * 授予域间push权限 由于链码没有区分，故与pull操作相同
     * @param fileId
     * @param role
     * @param username
     * @return
     */
    Boolean grantInnerChannelPush(String fileId, String role,String username);


    /**
     * 撤销域间pull权限
     * @param fileId
     * @param role
     * @param username
     * @return
     */
    Boolean revokeInnerChannelPull(String fileId, String role, String username);

    /**
     * 撤销域间push权限 底层与撤销pull权限一样
     * @param fileId
     * @param role
     * @param username
     * @return
     */
    Boolean revokeInnerChannelPush(String fileId, String role, String username);

    /**
     * pull文件操作   注意：如果成功，后面还需要加两次二次上链
     * @param requester 调用者
     * @param dataId  要pull的文件id
     * @param dataHash  dataHash
     * @param requesterChannelName 调用者所在channel 即 srcChannel 
     * @param dataChannelName 文件所在channel
     * @param copyDataId 复制后的dataId  需要先保存到数据库，如果出错再回滚
     * @return
     */
    ShareResult pullData(String requester, String dataId, String dataHash, String requesterChannelName, String dataChannelName, String copyDataId);


    /**
     * push文件操作   注意：如果成功，后面还需要加两次二次上链
     * @param requester 调用者
     * @param dataId  要push的文件id
     * @param dataHash  dataHash
     * @param requesterChannelName 调用者所在channel 即 srcChannel 
     * @param targetChannelName 目标域channel
     * @param copyDataId 复制后的dataId   需要先保存到数据库，如果出错再回滚
     * @return
     */
    ShareResult pushData(String requester, String dataId, String dataHash, String requesterChannelName, String targetChannelName, String copyDataId);

    ShareResult pushData(String requester, String dataId, String dataHash, String requesterChannelName, String targetChannelName, String targetChannelUsername,String copyDataId);



    /**
     * 对跨链分享出去的文件进行跨链追踪
     * @param requester  调用者
     * @param requesterChannelName  调用者所在channel
     * @param txId   文件被pull的时候产生的txId
     * @param targetChannelName  文件被分享到targetChannel  现在要在targetChannel上对文件进行追踪
     * @return
     */
    Record traceForwardCrossChain(String requester,String requesterChannelName,String txId, String targetChannelName);


    String getChannelUsernameByChannel(String channel);
    
}
