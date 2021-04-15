package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.entities.SharedDataAuthority;
import com.hust.keyRD.system.dao.SharedDataAuthorityDao;
import com.hust.keyRD.system.service.SharedDataAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SharedDataAuthorityServiceImpl implements SharedDataAuthorityService {
    @Resource
    SharedDataAuthorityDao sharedDataAuthorityDao;
    @Override
    public void addSharedDataAuthority(SharedDataAuthority sharedDataAuthority) {
        sharedDataAuthorityDao.addSharedDataAuthority(sharedDataAuthority);
    }

    @Override
    public void optOnSharedDataAuthority(SharedDataAuthority sharedDataAuthority) {
        sharedDataAuthorityDao.optOnSharedDataAuthority(sharedDataAuthority);
    }

    @Override
    public List<SharedDataAuthority> receiveAllSharedDataMsg() {
        return sharedDataAuthorityDao.receiveAllSharedDataMsg();
    }

    @Override
    public Integer checkSharedData(SharedDataAuthority sharedDataAuthority) {
        return sharedDataAuthorityDao.checkSharedData(sharedDataAuthority);
    }
}
