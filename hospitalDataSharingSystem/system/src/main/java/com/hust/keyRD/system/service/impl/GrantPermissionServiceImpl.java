package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.entities.DataAuthority;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.GrantPermissionService;
import com.hust.keyRD.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @program: fabricPlatform
 * @description: GrantPermissionServiceImpl
 * @author: zwh
 * @create: 2021-01-06 09:25
 **/
@Slf4j
@Service
public class GrantPermissionServiceImpl implements GrantPermissionService {

    @Autowired
    private FabricService fabricService;

    @Autowired
    private DataService dataService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @Override
    public Boolean grantUserPermissionOnFile(DataAuthority dataAuthority) {
        Integer fileChannelId = dataService.findDataById(dataAuthority.getDataSampleId()).getChannelId();
        String fileChannelName = channelService.findChannelById(fileChannelId).getChannelName();
        String fileId = String.valueOf(dataAuthority.getDataSampleId());
        String role = "role1";
        String user = userService.findUserById(dataAuthority.getUserId()).getUsername();
        String permission;
        switch (dataAuthority.getAuthorityKey()) {
            case 1:
                permission = "read";
                break;
            case 2:
                permission = "modify";
                break;
            case 3:
                permission = "delete";
                break;
            default:
                log.info("授权用户权限失败：invalid AuthorityKey");
                return false;
        }
        return fabricService.grantUserPermissionOnFileInnerChannel("org"+fileChannelId*2+"_admin",fileId, fileChannelName, permission, role, user);
    }

    @Override
    public Boolean revokeUserPermissionOnFile(DataAuthority dataAuthority) {
        Integer fileChannelId = dataService.findDataById(dataAuthority.getDataSampleId()).getChannelId();
        String fileChannelName = channelService.findChannelById(fileChannelId).getChannelName();
        String fileId = String.valueOf(dataAuthority.getDataSampleId());
        String role = "role1";
        String user = userService.findUserById(dataAuthority.getUserId()).getUsername();
        String permission;
        switch (dataAuthority.getAuthorityKey()) {
            case 1:
                permission = "read";
                break;
            case 2:
                permission = "modify";
                break;
            case 3:
                permission = "delete";
                break;
            default:
                log.info("撤销用户权限失败：invalid AuthorityKey");
                return false;
        }
        return fabricService.revokeUserPermissionOnFile("org"+fileChannelId*2+"_admin", fileChannelName,fileId, permission, role,user);
    }
}

