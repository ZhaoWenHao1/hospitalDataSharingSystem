package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.entities.Apply;
import com.hust.keyRD.commons.vo.ApplyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplyVOMapper {
    ApplyVOMapper INSTANCE = Mappers.getMapper(ApplyVOMapper.class);

    Apply toApply(ApplyVO applyVO);

    ApplyVO toApplyVO(Apply apply);
}
