package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.DataAuthority;

/**
 * @program: fabricPlatform
 * @description: 通过fabric进行授权service  虽然FabricService里已有类似函数，但参数复杂，这里进行封装简化
 * @author: zwh
 * @create: 2021-01-06 09:16
 **/
public interface GrantPermissionService {

    /**
     * 将某文件的某一权限授予用户
     * @param dataAuthority dataAuthority实体
     * @return success/fail
     */
    Boolean grantUserPermissionOnFile(DataAuthority dataAuthority);

    /**
     * 撤销某用户对某文件的某一权限
     * @param dataAuthority dataAuthority实体
     * @return success/fail
     */
    Boolean revokeUserPermissionOnFile(DataAuthority dataAuthority);

}
