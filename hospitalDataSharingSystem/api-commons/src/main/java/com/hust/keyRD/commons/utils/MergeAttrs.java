package com.hust.keyRD.commons.utils;

import com.google.common.base.Joiner;
import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.vo.AttributesVO;

import java.util.*;

public class MergeAttrs {
    public static List<String> mergeAttrs(List<String> origin){
        Set<String> set = new HashSet<>();
        List<AttributesVO> res = new ArrayList<>();
        List<String> result = new ArrayList<>();
        String stringList = Joiner.on(SystemConstant.SPLIT_SYMBOL).join(origin);
        String[] attrList = stringList.split(SystemConstant.SPLIT_SYMBOL);
        for (String s:attrList) {
            String[] kandv = s.split(":");
            if(!set.contains(kandv[0])){
                AttributesVO attributesVO = new AttributesVO(kandv[0], new HashSet<>(Collections.singletonList(kandv[1])));
                res.add(attributesVO);
                set.add(kandv[0]);
            }else {
                for (AttributesVO tmp:res) {
                    if(tmp.getKey().equals(kandv[0])){
                        tmp.getValue().add(kandv[1]);
                    }
                }
            }
        }
        for (AttributesVO attributesVO:res) {
            String tmp = attributesVO.getKey()+":";
            tmp += Joiner.on(SystemConstant.SPLIT_SYMBOL).join(attributesVO.getValue());
            result.add(tmp);
        }
        return result;
    }
}
