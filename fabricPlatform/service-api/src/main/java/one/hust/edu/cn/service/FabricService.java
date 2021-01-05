package one.hust.edu.cn.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface FabricService {
    String feignTest(int a, int b);

    /**
     * 调用链码  这里是指使用中心链上org1来进行权限管理
     * @param peers peer list
     * @param channelName channel名
     * @param ccName ccName
     * @param fcn 调用函数
     * @param args 链码调用参数
     * @return 结果
     */
    String invokeChaincode(String peers, String channelName, String ccName,
                           String fcn, List<String> args);

    /**
     * 授予 某角色 某些用户 某个channel的（文件）add权限
     * @param dstChannelName 目标channel  授予该channel的增加文件的权限
     * @param role 角色
     * @param username 用户名
     * @return 来自链码的授权结果
     */
    String grantUserPermission2Add(String dstChannelName, String role, String username);

    /**
     * 授予 某角色 某些用户 某个文件的查看 修改 删除 权限
     * @param dstChannelName 目标channel  授予该channel文件的权限
     * @param fileId 文件id
     * @param permission 权限 删改查
     * @param role 角色
     * @param users 用户列表
     * @return
     */
    String grantUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users);


    /**
     * 撤销 某角色 某些用户 某个文件的权限
     * @param dstChannelName 目标channel  授予该channel文件的权限
     * @param fileId 文件id
     * @param permission 权限 删改查
     * @param role 角色
     * @param users 用户列表
     * @return
     */
    String revokeUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users);

    /**
     * 跨链请求权限 操作链第一次上链
     * @param peers peer list
     * @param channelName channel名
     * @param ccName ccName
     * @param fcn 调用函数
     * @param args 链码调用参数
     * @return 事务号 tx_id
     */
    String crossAccess(String peers, String channelName, String ccName,
                           String fcn, List<String> args);

    /**
     * 申请创建文件权限  第一次上链
     * @param username 申请用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @return 事务id 或 空字符串
     */
    String applyForCreateFile(String username, String dstChannelName, String fileId);


    /**
     * 更新链上文件hash值  第二次上链
     * @param fileString
     * @param username
     * @param dstChannelName
     * @param fileId
     * @param txId
     * @return
     */
    String updateForCreateFile(String fileString, String username, String dstChannelName, String fileId, String txId);

    /**
     * 创建文件
     * @param fileString 文件字符串  将文件内容及属性拼接为字符串
     * @param username 用户名
     * @param dstChannelName 所创建的文件所在channel
     * @param fileId 文件id
     * @return
     */
    String createFile(String fileString, String username, String dstChannelName, String fileId) throws JsonProcessingException;

    /**
     * 请求对某个文件的 增删改查 权限  操作链第一次上链 返回事务号
     */

    /**
     * 第二次上链，更新链上数据hash值，实现与链下数据的同步
     * @param peers peer list
     * @param channelName channel名
     * @param ccName ccname
     * @param fcn 调用函数
     * @param args 链码调用参数
     * @param txId 第一次上链的事务号
     * @return 结果
     */
    String dataSyncRecord(String peers, String channelName, String ccName,
                       String fcn, List<String> args, String txId);

    /**
     * 完成对某个文件的增删改查  第二次上链  返回事务号
     */

    /**
     * 查询权限 如channel1上add的权限  由于权限管理由中心链完成，所以在本例中使用中心链上的org1来查询
     * @param obj channel name
     * @param opt 操作名 如 add
     * @return 权限对应的role和user
     */
    String getPolicy(String obj, String opt);

    /**
     * 根据事务号查询结构体信息
     * @param txId 事务号（申请权限的事务号）
     * @param order 第几次上链 1/2
     * @return 结构体JSON
     */
    String queryAuthority(String txId, String order);

    String argsTest(String peers, String channelName, String ccName,
                    String fcn, List<String> args);
}