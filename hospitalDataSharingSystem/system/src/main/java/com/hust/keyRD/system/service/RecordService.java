package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.Record;

/**
 * @program: hospitalDataSharingSystem
 * @description: RecordService
 * @author: zwh
 * @create: 2021-06-17 15:15
 **/
public interface RecordService {
    void addRecord(Record record);

    Record findByThisTx(String thisTxId);
    
    Record findRecentByDataId(Integer dataId);
    
    String generateTxId(Integer dataId);
}
