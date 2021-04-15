package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.vo.UserInnerDataVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @program: system
 * @description: UserInnerDataAuthorityVO mapper
 * @author: zwh
 * @create: 2021-03-20 18:08
 **/
@Mapper
public interface UserInnerDataVOMapper {
    UserInnerDataVOMapper INSTANCE = Mappers.getMapper(UserInnerDataVOMapper.class);
    
    @Mapping(target = "dataAuthoritySet", expression = "java(com.hust.keyRD.commons.vo.UserInnerDataVO.stringToSet(userInnerDataDto.getDataAuthorityList()))")
    UserInnerDataVO toUserInnerDataVO(UserInnerDataDto userInnerDataDto);
}
