package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: hospitalDataSharingSystem
 * @description: RecordDao
 * @author: zwh
 * @create: 2021-06-17 14:54
 **/
@Mapper
public interface RecordDao {
    
    void addRecord(Record record);
    
    Record findByThisTx(String thisTxId);
    
    Record findRecentByDataId(Integer dataId);
    
    
}
