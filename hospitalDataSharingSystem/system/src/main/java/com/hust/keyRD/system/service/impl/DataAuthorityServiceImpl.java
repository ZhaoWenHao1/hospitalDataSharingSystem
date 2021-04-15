package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserDataAuthDto;
import com.hust.keyRD.commons.entities.DataAuthority;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.vo.AllDataUserAuthorityVO;
import com.hust.keyRD.system.dao.DataAuthorityDao;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataAuthorityService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataAuthorityServiceImpl implements DataAuthorityService {
    @Resource
    DataAuthorityDao dataAuthorityDao;

    @Resource
    private UserService userService;

    @Resource
    private DataService dataService;

    @Resource
    private DataAuthorityService dataAuthorityService;

    @Resource
    private ChannelService channelService;

    @Override
    public void addDataAuthority(DataAuthority dataAuthority) {
        dataAuthorityDao.addDataAuthority(dataAuthority);
    }

    @Override
    public DataAuthority findDataAuthorityById(Integer id) {
        return dataAuthorityDao.findDataAuthorityById(id);
    }

    @Override
    public List<DataAuthority> findDataAuthorityByUserId(Integer userId) {
        return dataAuthorityDao.findDataAuthorityByUserId(userId);
    }

    @Override
    public List<DataAuthority> findDataAuthorityByDataId(Integer dataSampleId) {
        return dataAuthorityDao.findDataAuthorityByDataId(dataSampleId);
    }

    @Override
    public List<DataAuthority> findDataAuthorityByUserIdAndDataId(Integer userId, Integer dataSampleId) {
        return dataAuthorityDao.findDataAuthorityByUserIdAndDataId(userId, dataSampleId);
    }

    @Override
    public void editDataAuthority(Integer id, Integer authorityKey) {
        dataAuthorityDao.editDataAuthority(id, authorityKey);
    }

    @Override
    public Integer checkDataAuthority(DataAuthority dataAuthority) {
        return dataAuthorityDao.checkDataAuthority(dataAuthority);
    }

    @Override
    public void addMasterDataAuthority(Integer originUserId, Integer dataSampleId) {
        dataAuthorityDao.addMasterDataAuthority(originUserId, dataSampleId);
    }

    @Override
    public List<DataAuthority> getAllAuthority() {
        return dataAuthorityDao.getAllAuthority();
    }

    @Override
    public Integer deleteDataAuthority(DataAuthority dataAuthority) {
        return dataAuthorityDao.deleteDataAuthority(dataAuthority);
    }

    @Override
    public List<UserDataAuthDto> findUsersDataAuthority(Integer channelId) {
        return dataAuthorityDao.findUsersDataAuthority();
    }

    @Override
    public List<AllDataUserAuthorityVO> findUsersDataAuthorityVO() {
        List<AllDataUserAuthorityVO> result = new ArrayList<>();
        Set<List<Integer>> set = new LinkedHashSet<>();//保存用户Id与对应的文件id
        List<User> users = userService.getAllUser();
        List<DataSample> dataSamples = dataService.getDataList();
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < dataSamples.size(); j++) {
                List<Integer> s = new ArrayList<>();
                s.add(users.get(i).getId());
                s.add(dataSamples.get(j).getId());
                set.add(s);
            }
        }
        Iterator<List<Integer>> it = set.iterator();
        while (it.hasNext()) {
            List<Integer> tmp = it.next();
            List<DataAuthority> dataAuthorities = dataAuthorityService.findDataAuthorityByUserIdAndDataId(tmp.get(0), tmp.get(1));
            Set<Integer> authoritySet = new HashSet<>();
            for (int i = 0; i < dataAuthorities.size(); i++) {
                authoritySet.add(dataAuthorities.get(i).getAuthorityKey());
            }
            User user = userService.findUserById(tmp.get(0));
            DataSample dataSample = dataService.findDataById(tmp.get(1));
            AllDataUserAuthorityVO allDataUserAuthorityVO = new AllDataUserAuthorityVO();
            allDataUserAuthorityVO.setUserId(tmp.get(0));
            allDataUserAuthorityVO.setUserName(user.getUsername());
            allDataUserAuthorityVO.setChannelName(channelService.findChannelById(dataSample.getChannelId()).getChannelName());
            allDataUserAuthorityVO.setDataId(tmp.get(1));
            allDataUserAuthorityVO.setDataName(dataSample.getDataName());
            allDataUserAuthorityVO.setChannelAuthoritySet(authoritySet);
            result.add(allDataUserAuthorityVO);
        }
        return result;
    }
}
