package com.hust.keyRD.commons.vo;

import com.hust.keyRD.commons.entities.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVO {
    Record record;
    String fileName;
}
