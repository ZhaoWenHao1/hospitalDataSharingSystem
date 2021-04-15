package com.hust.keyRD.commons.vo;

import com.hust.keyRD.commons.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChannelVO {
    private User user;
    private String channelName;
}
