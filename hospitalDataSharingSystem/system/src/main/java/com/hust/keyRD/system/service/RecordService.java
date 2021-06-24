package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.Record;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021-06-24 09:12
 **/
public interface RecordService {
    void addRecord(Record record);

    Record findByThisTx(String thisTxId);

    Record findRecentByDataId(Integer dataId);

    String generateTxId(Integer dataId);
}
