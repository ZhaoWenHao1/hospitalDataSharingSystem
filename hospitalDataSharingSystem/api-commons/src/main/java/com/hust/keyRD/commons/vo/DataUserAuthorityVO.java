package com.hust.keyRD.commons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.hust.keyRD.commons.entities.DataSample;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUserAuthorityVO implements Serializable {
    private DataSample dataSample;
    private String channelName;//该文件所在的channel名称
    private Set<Integer> authoritySet;
}
