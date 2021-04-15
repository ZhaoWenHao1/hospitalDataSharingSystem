package com.hust.keyRD.system.controller;

import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.system.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ChannelController {
    @Resource
    ChannelService channelService;

    //获取所有channel
    @GetMapping(value = "/channel/getAllChannels")
    public CommonResult getAllChannels(){
        List<Channel> channels = channelService.getAllChannel();
        return new CommonResult<>(200,"获取所有channels成功",channels);
    }
}
