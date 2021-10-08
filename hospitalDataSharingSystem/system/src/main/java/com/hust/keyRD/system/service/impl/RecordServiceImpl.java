package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.system.dao.RecordDao;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021-06-17 15:16
 **/
@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordDao recordDao;

    @Autowired
    private ChannelService channelService;

    @Override
    public void addRecord(Record record) {
        record.setCreatedTime(new Timestamp(new Date().getTime()));
        recordDao.addRecord(record);
    }

    @Override
    public Record findByThisTx(String thisTxId) {
        return recordDao.findByThisTx(thisTxId);
    }

    @Override
    public Record findRecentByDataId(Integer dataId) {
        return recordDao.findRecentByDataId(dataId);
    }

    @Override
    public String generateTxId(Integer dataId) {
        int h1 = dataId.hashCode();
        int h2 = new Date().hashCode();
        int h = (h1 << 16) ^ (h2);
        return String.valueOf(h > 0 ? h : -h);
    }

    @Override
    public boolean record(String srcChain, String username, DataSample data, String typeTx){
        Record record = new Record();
        String channelName = channelService.findChannelById(data.getChannelId()).getChannelName();
        record.setHashData(String.valueOf(data.hashCode()));
        record.setSrcChain(srcChain);
        record.setDstChain(channelName);
        record.setUser(username);
        record.setDataId(data.getId());
        record.setTypeTx(typeTx);

        String thisTx = generateTxId(data.getId());
        Record lastRecord = findRecentByDataId(data.getId());
        String lastTx = lastRecord == null ? "0" : lastRecord.getThisTxId();
        record.setThisTxId(thisTx);
        record.setLastTxId(lastTx);
        addRecord(record);
        return true;
    }
}
