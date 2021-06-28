package com.hust.keyRD.system.api.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "fabric", url = "http://211.69.198.53:8000")
public interface FabricFeignService {
    @GetMapping("/add/")
    String add(@RequestParam("a") int a, @RequestParam("b") int b);

    @PostMapping("/invokeChaincode")
    Response invokeChaincodeTest(@RequestParam("requestor") String requester, @RequestParam("invoke_peers") String peers, @RequestParam("channel_name") String channelName, @RequestParam("cc_name") String ccName,
                                 @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

    @PostMapping("/crossAccess")
    Response crossAccess(@RequestParam("requestor") String requester, @RequestParam("listen_peer") String listenPeers,@RequestParam("invoke_peers") String peers, @RequestParam("src_channel") String channelName, @RequestParam("cc_name") String ccName,
                         @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

    @PostMapping("/dataSyncRecord")
    Response dataSyncRecord(@RequestParam("peers") String peers, @RequestParam("channel_name") String channelName, @RequestParam("cc_name") String ccName,
                         @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args, @RequestParam("tx_id") String txId);

    @PostMapping("/traceBackward")
    Response traceBackward(@RequestParam("data_id") String dataId, @RequestParam("file_channel") String fileChannel, @RequestParam("tx_id") String txId);

    @PostMapping("/traceBackward")
    Response traceBackward(@RequestParam("data_id") String dataId, @RequestParam("file_channel") String fileChannel);
    
    /*
        查询权限 如channel1上add的权限  由于权限管理由中心链完成，所以在本例中使用中心链上的org1来查询
     */
    @PostMapping("/getPolicy")
    Response getPolicy(@RequestParam("requestor") String requester,@RequestParam("channel_name") String channelName, @RequestParam("invoke_peers") String invoke_peers,@RequestParam("obj") String obj, @RequestParam("opt") String opt);

    /*
        本例子中文件都是在channel1上进行操作，所以对应的权限也都是在channel1上，这里默认用channel1上的org3来查询
     */
    @PostMapping("/query")
    Response queryAuthority(@RequestParam("tx_id") String txId, @RequestParam("order") String order);

    @PostMapping("/argsTest/")
    String argsTest(@RequestParam("peers") String peers, @RequestParam("channel_name") String channelName, @RequestParam("cc_name") String ccName,
                    @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

    @PostMapping("/trace_forward")
    String traceForwardCrossChain(@RequestParam("requestor") String requester, @RequestParam("listen_peer") String listenPeers, @RequestParam("invoke_peers") String peers, @RequestParam("src_channel") String channelName, @RequestParam("cc_name") String ccName,
                                  @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

    @PostMapping("/pullFileAcrossChannel")
    String pullFileAcrossChannel(@RequestParam("requestor") String requester, @RequestParam("listen_peer") String listenPeers, @RequestParam("invoke_peers") String peers, @RequestParam("src_channel") String channelName, @RequestParam("cc_name") String ccName,
                                 @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);


    // 文件加密策略上链
    @GetMapping("addCryptographAndPolicy")
    Response addEncryptionPolicy(@RequestParam("requestor") String requester, @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers, @RequestParam("cc_name") String ccName,
                                 @RequestParam("fcn") String fcn, @RequestParam("policy_root") String policy, @RequestParam("args") List<String> args);

    // 新增用户
    @GetMapping("addUser")
    Response addUser(@RequestParam("requestor") String requester, @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers, @RequestParam("cc_name") String ccName,
                     @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

    // 申请权限
    @GetMapping("askForPermissions")
    Response applyForAttribute(@RequestParam("requestor") String requester, @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers, @RequestParam("cc_name") String ccName,
                               @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

    // 属性审核  如果通过就把解密信息上链
    @GetMapping("addCryptograph")
    Response crossChannelJudgement(@RequestParam("requestor") String requester, @RequestParam("channel_name") String channelName, @RequestParam("peers") String peers, @RequestParam("cc_name") String ccName,
                                   @RequestParam("fcn") String fcn, @RequestParam("args") List<String> args);

//    @GetMapping("viewAllDecryptionOperationNumbersOfTheFile")
//    Response getDateRecord()

}
