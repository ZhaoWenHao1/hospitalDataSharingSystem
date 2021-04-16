package com.hust.keyRD.commons.vo;

import lombok.Data;

/**
 * @program: hospitalDataSharingSystem
 * @description: 上次文件结果
 * @author: zwh
 * @create: 2021-04-16 10:52
 **/
@Data
public class UploadResult {
    // 上传文件一次上链交易号
    private String firstUpChainTx;
    // 上传文件二次上链交易号
    private String secondUpChainTx;
    // 授权用户文件读取权限：true/false
    private String grantReadRes;
    // 授权用户文件修改权限：true/false
    private String grantModifyRes;
}
