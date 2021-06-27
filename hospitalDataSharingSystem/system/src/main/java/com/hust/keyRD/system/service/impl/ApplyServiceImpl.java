package com.hust.keyRD.system.service.impl;

import com.google.common.base.Joiner;
import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.entities.Apply;
import com.hust.keyRD.commons.vo.ApplyVO;
import com.hust.keyRD.commons.vo.AttributesVO;
import com.hust.keyRD.commons.vo.mapper.ApplyVOMapper;
import com.hust.keyRD.system.dao.ApplyDao;
import com.hust.keyRD.system.dao.UserDao;
import com.hust.keyRD.system.service.ApplyService;
import io.micrometer.core.instrument.util.StringUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
     * 获取向当前用户申请的列表
     * @param userId
     * @return
     */
    @Override
    public List<ApplyVO> getApplyList(Integer userId) {
        List<Apply> applyList = applyDao.getApplyListFrom(userId);
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

    /**
     * 获取个人属性
     * @param userId
     * @return
     */
    @Override
    public  List<String> getUserAttributes(Integer userId) {
        String myAttributes = applyDao.getUserAttributesByUserId(userId);
        if(StringUtils.isBlank(myAttributes)){
            return null;
        }
        String[] attributesList = myAttributes.split(SystemConstant.SPLIT_SYMBOL);
        return new ArrayList<>(Arrays.asList(attributesList));
    }

    /**
     * 获取当前用户的申请列表
     * @param userId
     * @return
     */
    @Override
    public List<ApplyVO> getUserApplyAttributes(Integer userId) {
        List<Apply> applyList = applyDao.getApplyListTo(userId);
        List<ApplyVO> res = new ArrayList<>();
        for (Apply apply:applyList) {
            String[] split = apply.getAttributes().split(SystemConstant.SPLIT_SYMBOL);
            for (String s:split) {
                ApplyVO applyVO = ApplyVOMapper.INSTANCE.toApplyVO(apply);
                applyVO.setAttributes(s);
                applyVO.setApplierName(userDao.findUserById(apply.getApplierId()).getUsername());
                applyVO.setApplyTime(apply.getApplyTime());
                applyVO.setTargetUserName(userDao.findUserById(apply.getTargetUserId()).getUsername());
                res.add(applyVO);
            }
        }
        return res;
    }

    @Override
    public List<AttributesVO> getAttributesList() {
        String stringList = Joiner.on(SystemConstant.SPLIT_SYMBOL).join(applyDao.getAllAttributes());
        Set<String> set = new HashSet<>();
        List<AttributesVO> res = new ArrayList<>();
        String[] attrList = stringList.split(SystemConstant.SPLIT_SYMBOL);
        for (String s:attrList) {
            String[] kandv = s.split(":");
            if(!set.contains(kandv[0])){
                AttributesVO attributesVO = new AttributesVO(kandv[0], new HashSet<>(Collections.singletonList(kandv[1])));
                res.add(attributesVO);
                set.add(kandv[0]);
            }else {
                for (AttributesVO tmp:res) {
                    if(tmp.getKey().equals(kandv[0])){
                        tmp.getValue().add(kandv[1]);
                    }
                }
            }
        }
        return res;
    }
}