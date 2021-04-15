package com.hust.keyRD.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAuthority implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer dataSampleId;
    private Integer authorityKey;//用户权限 1代表查看文件 2代表修改文件 3代表删除文件
}
