package com.hust.keyRD.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apply {
    private Integer id;
    private Integer applierId;
    private String applyRemarks;
    private Integer targetUserId;
    private String checkRemarks;
    private String attributes;
    private Date applyTime;
    private Integer result;
    private Integer state;
}
