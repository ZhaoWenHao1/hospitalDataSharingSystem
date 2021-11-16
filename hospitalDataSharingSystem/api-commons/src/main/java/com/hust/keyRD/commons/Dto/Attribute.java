package com.hust.keyRD.commons.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: hospitalDataSharingSystem
 * @description: 属性
 * @author: zwh
 * @create: 2021-06-28 11:30
 **/
@Data
@AllArgsConstructor
public class Attribute {
    // 属性名称
    private String name;
    // 属性值
    private String value;

    // 根据 name:value的字符串获得对象
    public static Attribute parse(String attrStr) {
        int idx = attrStr.indexOf(":");
        String name = attrStr.substring(0, idx);
        String value = attrStr.substring(idx + 1);
        return new Attribute(name.trim(), value.trim());
    }

    /**
     * 从形如这样的 position:teacher,academy:agriculture 字符串中解析出属性并用 & 连接：teacher & agriculture
     *
     * @param attrStr
     * @return
     */
    public static String getAttrs(String attrStr) {
        StringBuilder sb = new StringBuilder();
        String[] strings = attrStr.split(",");
        for (int i = 0; i < strings.length; i++) {
            sb.append(parse(strings[i]).getValue());
            if (i != strings.length - 1) {
                sb.append(" &fgf& ");
            }
        }
        return sb.toString();
    }

}
