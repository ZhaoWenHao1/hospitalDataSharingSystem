package com.hust.keyRD.system.api.v2.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "fabric2", url = "http://101.201.49.180:8000")
public interface FeignService {
    @PostMapping("/putaddattr")
    Response addAttr(@RequestParam("channel_name") String channelName, @RequestParam("args") List<String> args);
//    Response addAttr(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
//                      @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);

    @PostMapping("/putattruser")
    Response attrUser(@RequestParam("channel_name") String channelName, @RequestParam("args") List<String> args);
//    Response attrUser(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
//                      @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);

    @PostMapping("/putshare_judgement")
    Response judgement(@RequestParam("channel_name") String channelName,@RequestParam("args") List<String> args);
//    Response judgement(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
//                       @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);

    @PostMapping("/putattrpolicy")
    Response attrPolicy(@RequestParam("channel_name") String channelName, @RequestParam("args") List<String> args);
//    Response attrPolicy(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
//                        @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);
}
