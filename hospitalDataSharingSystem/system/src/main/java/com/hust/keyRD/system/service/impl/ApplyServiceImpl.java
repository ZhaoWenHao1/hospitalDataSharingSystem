package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.entities.Apply;
import com.hust.keyRD.commons.vo.ApplyVO;
import com.hust.keyRD.commons.vo.mapper.ApplyVOMapper;
import com.hust.keyRD.system.dao.ApplyDao;
import com.hust.keyRD.system.dao.UserDao;
import com.hust.keyRD.system.service.ApplyService;
import com.hust.keyRD.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("applyService")
public class ApplyServiceImpl implements ApplyService {
    @Resource
    ApplyDao applyDao;
    @Resource
    UserDao userDao;

    /**
     * 保存请求
     * @param apply
     */
    @Override
    public void save(Apply apply) {
        applyDao.save(apply);
    }

    /**
     * 获取当前用户的请求列表
     * @param userId
     * @return
     */
    @Override
    public List<ApplyVO> getApplyList(Integer userId) {
        List<Apply> applyList = applyDao.getApplyList(userId);
        List<ApplyVO> res = new ArrayList<>();
        for (Apply apply:applyList) {
            ApplyVO applyVO = ApplyVOMapper.INSTANCE.toApplyVO(apply);
            applyVO.setApplierName(userDao.findUserById(apply.getApplierId()).getUsername());
            applyVO.setApplyTime(apply.getApplyTime());
            res.add(applyVO);
        }
        return res;
    }

    /**
     * 检查用户是否拥有属性
     * @param userId
     * @param attributes
     * @return
     */
    @Override
    public boolean checkAttribute(Integer userId, String attributes) {
        if(userDao.checkAttribute(userId,attributes)==0){
            return false;
        }
        return true;
    }

    /**
     * 根据id找请求
     * @param id
     * @return
     */
    @Override
    public Apply findApplyById(Integer id) {
        return applyDao.findApplyById(id);
    }

    @Override
    public void update(Apply hasDownApply) {
        applyDao.updateResultAndState(hasDownApply);
    }
}
