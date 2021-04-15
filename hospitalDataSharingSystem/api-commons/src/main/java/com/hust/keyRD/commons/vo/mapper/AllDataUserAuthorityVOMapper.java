package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.Dto.UserDataAuthDto;
import com.hust.keyRD.commons.vo.AllDataUserAuthorityVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @program: fabricPlatform
 * @description:
 * @author: zwh
 * @create: 2021/2/3 11:58
 **/
@Mapper
public interface AllDataUserAuthorityVOMapper {
    AllDataUserAuthorityVOMapper INSTANCE = Mappers.getMapper(AllDataUserAuthorityVOMapper.class);


    @Mapping(target = "channelAuthoritySet", expression = "java(com.hust.keyRD.commons.vo.AllDataUserAuthorityVO.stringToSet(userDataAuthDto.getDataAuthorityList()))")
    AllDataUserAuthorityVO toAllDataUserAuthorityVO(UserDataAuthDto userDataAuthDto);
}
