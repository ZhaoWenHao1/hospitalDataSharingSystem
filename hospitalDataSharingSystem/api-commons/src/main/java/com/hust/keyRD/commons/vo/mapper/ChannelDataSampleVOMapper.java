package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.vo.ChannelDataSampleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @program: system
 * @description: ChannelDataSampleVO mapper
 * @author: zwh
 * @create: 2021-03-20 13:38
 **/
@Mapper
public interface ChannelDataSampleVOMapper {
    ChannelDataSampleVOMapper INSTANCE = Mappers.getMapper(ChannelDataSampleVOMapper.class);
    
    public ChannelDataSampleVO toChannelDataSampleVO(DataSample dataSample);
}
