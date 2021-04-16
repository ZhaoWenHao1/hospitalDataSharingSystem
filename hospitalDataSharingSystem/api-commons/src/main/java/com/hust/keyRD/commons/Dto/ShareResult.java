package com.hust.keyRD.commons.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: system
 * @description: pull / push文件时fabric返回的结构体
 * @author: zwh
 * @create: 2021-03-30 15:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareResult {
    
    @JsonProperty("result")
    private Boolean result;
    
    @JsonProperty("err_kinds")
    private String errKinds;
    
    @JsonProperty("tx_id")
    private String txId;

    /**
     * 目标域二次上链结果
     */
    private String targetChannelResponse;

    /**
     * 发起域二次上链结果
     */
    private String srcChannelResponse;

    @Override
    public String toString() {
        return "ShareResult{" +
                "result=" + result +
                ", errKinds='" + errKinds + '\'' +
                ", txId='" + txId + '\'' + 
                ", \n       targetChannelResponse='" + targetChannelResponse + '\''  +
                ", \n       srcChannelResponse='" + srcChannelResponse + '\'' +
                '}';
    }
}
