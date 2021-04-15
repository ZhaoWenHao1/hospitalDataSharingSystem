package com.hust.keyRD.commons.vo.mapper;

import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.vo.DataSampleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.xml.crypto.Data;

/**
 * @program: system
 * @description: DataSampleVOMapper
 * @author: zwh
 * @create: 2021-03-22 11:47
 **/
@Mapper
public interface DataSampleVOMapper {
    
    DataSampleVOMapper INSTANCE = Mappers.getMapper(DataSampleVOMapper.class);
    
    DataSampleVO toDataSampleVO(DataSample dataSample);
}
