package com.hust.keyRD.commons.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: fabricTest
 * @description: 链上记录(Record)结构体
 * @author: zwh
 * @create: 2021-01-07 15:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Record {
    
    
    /**
     * 数据密文hash
     */
    @JsonProperty("hash_data")
    private String hashData;

    /**
     * 源链标识
     */
    @JsonProperty("src_chain")
    private String srcChain;

    /**
     * 用户标识
     */
    @JsonProperty("user")
    private String user;

    /**
     * 目标链标识
     */
    @JsonProperty("dst_chain")
    private String dstChain;

    /**
     * 目标数据标识
     */
    @JsonProperty("data_id")
    private String dataId;

    /**
     * 事务的操作类型
     */
    @JsonProperty("type_tx")
    private String typeTx;

    /**
     * 本次事务号
     */
    @JsonProperty("this_tx_id")
    private String thisTxId;

    /**
     * 上次事务号
     */
    @JsonProperty("last_tx_id")
    private String lastTxId;
}
