package com.hust.keyRD.commons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDataUserAuthorityVO {
    private Integer userId;
    private String userName;
    private Integer channelId;
    private String channelName;
    private String hospitalName;
    private Integer dataId;
    private String dataName;
    private Set<Integer> channelAuthoritySet;

    /**
     * 逗号隔开的字符串转Set
     * @param authSetStr
     * @return
     */
    public static Set<Integer> stringToSet(String authSetStr){
        if(authSetStr == null || authSetStr.isEmpty()){
            return new HashSet<>();
        }
        return Arrays.stream(authSetStr.split(",")).map(Integer::valueOf).collect(Collectors.toSet());
    }
}
