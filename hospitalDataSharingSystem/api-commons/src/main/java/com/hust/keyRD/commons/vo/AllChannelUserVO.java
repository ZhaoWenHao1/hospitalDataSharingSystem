package com.hust.keyRD.commons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllChannelUserVO {
    private Integer userId;
    private String userName;
    private Integer channelId;
    private String channelName;
    private Set<Integer> channelAuthoritySet;
}
