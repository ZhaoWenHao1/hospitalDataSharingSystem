package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.Apply;
import com.hust.keyRD.commons.vo.ApplyVO;
import com.hust.keyRD.commons.vo.AttributesVO;

import java.util.List;
import java.util.Map;

public interface ApplyService {
    void save(Apply apply);

    List<ApplyVO> getApplyList(Integer userId);

    boolean checkAttribute(Integer userId, String attributes);

    Apply findApplyById(Integer id);

    void update(Apply hasDownApply);

    List<String> getUserAttributes(Integer userId);

    List<ApplyVO> getUserApplyAttributes(Integer userId);

    List<AttributesVO> getAttributesList();

    /**
     * 获取所有属性： attrValue
     * @return
     */
    List<String> getAllAttributes();
}
