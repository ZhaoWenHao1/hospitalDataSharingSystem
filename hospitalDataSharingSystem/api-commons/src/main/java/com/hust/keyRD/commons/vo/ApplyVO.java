package com.hust.keyRD.commons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyVO {
    private Integer id;
    private String applierName;
    private String applyRemarks;
    private String targetUserName;
    private String checkRemarks;
    private String attributes;
    private Date applyTime;
    private Integer result;
}
