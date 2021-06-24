package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.Apply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplyDao {
    void save(Apply apply);

    List<Apply> getApplyListFrom(Integer userId);

    Apply findApplyById(Integer id);

    void updateResultAndState(Apply apply);

    String getUserAttributesByUserId(Integer userId);

    List<Apply> getApplyListTo(Integer userId);
}
