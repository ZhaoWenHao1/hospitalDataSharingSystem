package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.vo.AllChannelUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @program: system
 * @description: AllDataUserAuthorityVO 与 UserChannelAuthDto 的映射类
 * @author: zwh
 * @create: 2021-01-15 14:22
 **/
@Mapper
public interface AllChannelUserAuthorityVOMapper {
    AllChannelUserAuthorityVOMapper INSTANCE = Mappers.getMapper(AllChannelUserAuthorityVOMapper.class);

    @Mapping( target = "channelAuthoritySet",expression = "java(com.hust.keyRD.commons.vo.AllDataUserAuthorityVO.stringToSet(userChannelAuthDto.getAuthSet()))")
    AllChannelUserVO toAllDataUserAuthorityVO(UserChannelAuthDto userChannelAuthDto);
}
