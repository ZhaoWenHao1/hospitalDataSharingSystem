package com.hust.keyRD.system.api.v2.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "fabric2", url = "http://47.93.101.156:8000")
public interface FeignService {
    @PostMapping("/addattr")
    Response addAttr(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
                      @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);

    @PostMapping("/attruser")
    Response attrUser(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
                      @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);

    @PostMapping("/judgement")
    Response judgement(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
                       @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);

    @PostMapping("/attrpolicy")
    Response attrPolicy(@RequestParam("requestor") String requester,  @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers,
                        @RequestParam("fcn") String fcn, @RequestParam("cc_name") String ccName, @RequestParam("args") List<String> args);
}
