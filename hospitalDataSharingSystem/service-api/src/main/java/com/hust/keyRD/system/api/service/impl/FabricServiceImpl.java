package com.hust.keyRD.system.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.commons.utils.HashUtil;
import com.hust.keyRD.system.api.Constants.FabricConstant;
import com.hust.keyRD.system.api.feign.FabricFeignService;
import com.hust.keyRD.system.api.service.FabricService;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FabricServiceImpl implements FabricService {

    @Autowired
    private FabricFeignService fabricFeignService;


    @Override
    public String invokeChaincode(String requester, String peers, String channelName, String ccName,
                                  String fcn, List<String> args) {
        Response response = fabricFeignService.invokeChaincodeTest(requester, peers, channelName, ccName, fcn, args);
        return response.body().toString();
    }

    @Override
    public Boolean addPolicyInnerChannel(String requester, String channelName, String peers, String obj, String permission, String role, List<String> users) {
        String ccName = "Policy";
        String fcn = "AddPolicy";
        List<String> args = new ArrayList<String>() {{
            add(obj);
            add(permission);
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(requester, peers, channelName, ccName, fcn, args);
        if (response.contains("AddPolicy")) {
            log.info("AddPolicy {} {}成功", obj, permission);
            log.info("AddPolicy {}在{}上{}文件的权限成功,info:{}", users, obj, permission, response);
            return true;
        } else if (response.contains("exists")) {
            log.warn("AddPolicy {} {}失败，权限已存在", obj, permission);
            return false;
        } else {
            log.error("AddPolicy失败，info: {}", response);
            throw new FabricException("AddPolicy失败,info: " + response);
        }
    }


    @Override
    public Boolean updatePolicyInnerChannel(String requester, String channelName, String obj, String permission, String func, String role, List<String> users) {
        String peers = getPeers(requester);
        String ccName = "Policy";
        String fcn = "UpdatePolicy";
        List<String> args = new ArrayList<String>() {{
            add(obj);
            add(permission);
            add(func);
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(requester,peers, channelName, ccName, fcn, args);
        if (response.contains("UpdatePolicy")) {
            return true;
        } else {
            throw new FabricException("updatePolicy 失败，info: " + response);
        }

    }


    @Override
    public Boolean grantUserPermission2Add(String requester, String dstChannelName, String role, String username) {
        String peers = getPeers(requester);
        // 获得区块链对应的名字
        username = getChannelUsername(username);
        String response = getPolicy(requester,dstChannelName,dstChannelName, "add");
        try {
            if (response.contains("user_history")) {
                // 正常获得policy，即policy已存在
                // 由于是链内，所以现在目标链与调用链都是用户所在的链
                return updatePolicyInnerChannel(requester, dstChannelName,dstChannelName, "add", "adduser", role, Collections.singletonList(username));
            } else {
                
                return addPolicyInnerChannel(requester,dstChannelName,peers,dstChannelName, "add", role, Collections.singletonList(username));
            }
        } catch (FabricException e) {
            log.error("授权用户{}在{}上{}文件的权限失败,info:{}", username, dstChannelName, "add", response);
            throw e;
        }


    }

    @Override
    public Boolean grantUserPermissionOnFileInnerChannel(String requester,String fileId, String fileChannelName, String permission, String role, String username) {
        String obj = fileId;
        String peers = getPeers(requester);
        String response = getPolicy(requester,fileChannelName,obj, permission);
        // 获得区块链对应的名字
        username = getChannelUsername(username);
        try {
            if (response.contains("user_history")) {
                // 正常获得policy，即policy已存在
                return updatePolicyInnerChannel(requester,fileChannelName,obj,permission, "adduser", role, Collections.singletonList(username));
            } else {
                // policy不存在 增加policy
                return addPolicyInnerChannel(requester,fileChannelName,peers,obj, permission, role, Collections.singletonList(username));
            }
        } catch (FabricException e) {
            log.error("授权用户{}对文件{}的{}权限失败,info:{}", Collections.singletonList(username), fileId, permission, response);
            throw e;
        }
    }


    /**
     * 撤销用户权限
     *
     * @param obj   channelName或fileId
     * @param opt   权限
     * @param role  角色
     * @param users users
     * @return
     */
    private Boolean revokeUserPermission(String obj, String opt, String role, List<String> users) {
        // 由于该例子中授权都由中心链上的org1完成 暂时写死
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "UpdatePolicy";
        List<String> args = new ArrayList<String>() {{
            add(obj);
            add(opt);
            add("deleteuser");
            add(role);
            addAll(users);
        }};
//        String response = invokeChaincode(peers, channelName, ccName, fcn, args);
        String response = "invokeChaincode(peers, channelName, ccName, fcn, args)";
        if (response.contains("Success")) {
            log.info("撤销用户{}在{}上的{}权限成功,info:{}", users, obj, opt, response);
            return true;
        } else {
            log.warn("撤销用户{}在{}上的{}权限失败,info:{}", users, obj, opt, response);
            throw new FabricException("撤销权限失败,info: " + response);
        }
    }

    @Override
    public Boolean revokeUserPermissionOnFile(String requester, String channelName,String fileId, String permission, String role, String username) {
        String channelUsername = getChannelUsername(username);
        return updatePolicyInnerChannel(requester, channelName,fileId, permission, "deleteuser", role, Collections.singletonList(channelUsername));

    }

    @Override
    public Boolean revokeUserPermission2Add(String requester, String channelName,String role, String username) {
        String channelUsername = getChannelUsername(username);
        return updatePolicyInnerChannel(requester, channelName,channelName, "add", "deleteuser", role, Collections.singletonList(channelUsername));
    }

    @Override
    public String crossAccess(String peers, String channelName, String ccName, String fcn, List<String> args) {
        Response response = fabricFeignService.crossAccess(peers, channelName, ccName, fcn, args);
        return response.body().toString();
    }

    private String applyForOptFile(String requester, String channelName,String fileHash, String fileId,String type) {
        String peers = getPeers(requester);
        String channelUserName = getChannelUsername(requester);
        String fcn = "chainAuditRecord";
        String ccName = "record";
        List<String> args = new ArrayList<String>(){{
            add(fileHash);
            add(channelName);
            add(channelUserName);
            add(fileId);
            add(type);
        }};
        String response = invokeChaincode(requester,peers,channelName,ccName,fcn,args);
        if (response.contains("hash_data")) {
            log.info("applyForCreateFile {} {} {}成功", requester, type, fileId);
            try{
                // 获取上链事务id
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);
                return root.path("this_tx_id").asText();
            }catch (IOException e){
                throw new FabricException("创建文件失败，info: 无法解析出this_tx_id" );
            }

        } else {
            log.error("applyForCreateFile失败，info: {}", response);
            throw new FabricException("applyForCreateFile失败,info: " + response);
        }
    }

    @Override
    public String applyForCreateFile(String requester, String channelName,String fileHash, String fileId) {
        return applyForOptFile(requester, channelName,fileHash,fileId, "add");
        
        
//        try {
//            String txId = applyForOptFile(fileHash, srcChannelName, username, dstChannelName, fileId, "add");
//            if (txId == null || txId.isEmpty()) {
//                throw new FabricException("获取创建文件权限失败");
//            } else {
//                return txId;
//            }
//        } catch (IOException e) {
//            log.error("用户{}获取创建{}文件权限失败", username, dstChannelName);
//            throw new FabricException("获取创建文件权限失败");
//        }

    }

    @Override
    public String applyForReadFile(String requester, String channelName,String fileHash, String fileId) {
        return applyForOptFile(requester, channelName, fileHash, fileId, "read");
    }

    @Override
    public String applyForModifyFile(String requester, String channelName,String fileHash, String fileId) {
        return applyForOptFile(requester, channelName, fileHash, fileId, "modify");
    }

//    // 二次上链
//    private Record updateForOptFile(String fileString, String srcChannelName, String username, String dstChannelName, String fileId, String txId, String opt) {
//        String hashSHA1 = HashUtil.hash(fileString, "SHA1");
//        String peers;
//        if (dstChannelName.equals("channel1")) {
//            peers = "peer0.org3.example.com";
//        } else {
//            peers = "peer0.org5.example.com";
//        }
//        String channelName = dstChannelName; // 第二次上链由目标链节点调用链码 更新目标链记录 
//        String ccName = "record";
//        String fcn = "dataSyncRecord";
//        final String fileIdFinal = fileId + FabricConstant.Separator + dstChannelName;
//        List<String> args = new ArrayList<String>() {{
//            add(hashSHA1);
//            add(srcChannelName);
//            add(username);
//            add(dstChannelName);
//            add(fileIdFinal);
//            add(opt);
//        }};
//        // 成功返回Record对象json  失败就无法正常进行json解析
//        String response = dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String recordJson = response.replaceAll("\\\\", "");
//            recordJson = recordJson.substring(1, recordJson.length() - 1);
//            return mapper.readValue(recordJson, Record.class);
//        } catch (IOException e) {
//            log.error("二次上链更新hash失败,username:{},dstChannelName:{},fileId:{},txId:{},opt:{}, response:{}", username, dstChannelName, fileId, txId, opt, response);
//            throw new FabricException("二次上链更新hash失败");
//        }
//
//    }

    @Override
    public String updateForCreateFile(String requester, String channelName,String fileHash, String fileId,String txId) {
        return dataSyncRecord(requester,channelName,fileHash,fileId,"add",txId);
    }

    @Override
    public String updateForReadFile(String requester, String channelName,String fileHash, String fileId,String txId) {
        return dataSyncRecord(requester,channelName,fileHash,fileId,"read",txId);
    }

    @Override
    public String updateForModifyFile(String requester, String channelName,String fileHash, String fileId,String txId) {
        return dataSyncRecord(requester,channelName,fileHash,fileId,"modify",txId);
    }


    @Override
    public String dataSyncRecord(String requester, String channelName,String fileHash, String fileId, String typeTx,String txId) {
        String peers = getPeers(requester);
        String channelUserName = getChannelUsername(requester);
        String fcn = "chainSyncRecord";
        String ccName = "record";
        List<String> args = new ArrayList<String>(){{
            add(fileHash);
            add(channelName);
            add(channelUserName);
            add(fileId);
            add(typeTx);
            add(txId);
        }};
        String response = invokeChaincode(requester,peers,channelName,ccName,fcn,args);
        if (response.contains("hash_data")) {
            log.info("dataSyncRecord {} {} {}成功", requester, typeTx, fileId);
            try{
                // 获取上链事务id
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);
                return root.path("this_tx_id").asText();
            }catch (IOException e){
                throw new FabricException("第二次上链失败，info: 无法解析出this_tx_id" );
            }

        } else {
            log.error("dataSyncRecord失败，info: {}", response);
            throw new FabricException("dataSyncRecord失败,info: " + response);
        }
    }

    private Record parseRecordFromResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        String recordJson = root.toString().replaceAll("\\\\", "");
        return mapper.readValue(recordJson, Record.class);
    }

    @Override
    public Record traceBackward(String requester, String channelName, String fileId) {
        String peers = getPeers(requester);
        String fcn = "traceBackward";
        String ccName = "record";
        List<String> args = new ArrayList<String>(){{
            add(fileId);
        }};
        String response = invokeChaincode(requester,peers,channelName,ccName,fcn,args);
        try {
            return parseRecordFromResponse(response);
        } catch (IOException e) {
            log.error("溯源失败,fileId:{}, response:{}", fileId, response);
            throw new FabricException("溯源失败: fileId:" + fileId);
        }
    }

    @Override
    public Record traceBackward(String requester, String channelName, String fileId, String txId) {
        String peers = getPeers(requester);
        String fcn = "traceBackward";
        String ccName = "record";
        List<String> args = new ArrayList<String>(){{
            add(fileId);
            add(txId);
        }};
        String response = invokeChaincode(requester,peers,channelName,ccName,fcn,args);
        try {
            return parseRecordFromResponse(response);
        } catch (IOException e) {
            log.error("溯源失败,fileId:{}, response:{}", fileId, response);
            throw new FabricException("溯源失败: fileId:" + fileId);
        }
    }

    @Override
    public List<Record> traceBackwardAll(String requester, String channelName, String fileId) {
        List<Record> ans = new ArrayList<>();
        Record record = traceBackward(requester, channelName, fileId);
        ans.add(record);
        ans.addAll(traceBackwardAll(requester,channelName,fileId, record.getThisTxId()));
        return ans;
    }

    @Override
    public List<Record> traceBackwardAll(String requester, String channelName, String fileId, String txId) {
        List<Record> ans = new ArrayList<>();
        Record record = traceBackward(requester, channelName, fileId,txId);
        ans.add(record);
        while (!record.getLastTxId().equals("0")) {
            record = traceBackward(requester,channelName,fileId, record.getThisTxId());
            ans.add(record);
        }
        return ans;
    }

    @Override
    public String getPolicy(String requester,String channelName,String obj, String opt) {
        String peers = getPeers(requester);
        Response response = fabricFeignService.getPolicy(requester,channelName,peers,obj, opt);
        return response.body().toString();
    }

    @Override
    public String getPolicy(String fileId, String channelName, String opt) {
//        String obj = fileId + FabricConstant.Separator + channelName;
//        Response response = fabricFeignService.getPolicy(obj, opt);
//        return response.body().toString();
        // TODO
        return null;
    }

    @Override
    public String getCentrePolicy(String fileId) {
        String requester = "org1_admin";
        String peers = getPeers(requester);
        String channelName = "centre";
        String ccName = "audit";
        List<String> args = new ArrayList<String>(){{
            add(fileId);
            add("share");
        }};
        String fcn = "GetPol";
        Response response = fabricFeignService.invokeChaincodeTest(requester, peers, channelName, ccName, fcn, args);
        return response.body().toString();
    }

    @Override
    public String queryAuthority(String txId, String order) {
        Response response = fabricFeignService.queryAuthority(txId, order);
        return response.body().toString();
    }

    @Override
    public String argsTest(String peers, String channelName, String ccName, String fcn, List<String> args) {
        return fabricFeignService.argsTest(peers, channelName, ccName, fcn, args);
    }


    @Override
    public Boolean grantInnerChannelPull(String fileId, String role,String username){
        String res = getCentrePolicy(fileId);
        String requester = "org1_admin";
        String peers = getPeers(requester);
        String channelUsername = getChannelUsername(username);
        String channelName = "centre";
        String ccName = "audit";
        String fcn;
        List<String> args = new ArrayList<String>();
        if(res.contains("obj")){
            // 权限已存在，添加用户
            fcn = "UpdatePolicy";
            args.add(fileId);
            args.add("share");
            args.add("adduser");
            args.add(role);
            args.add(channelUsername);
        }else{
            // 权限不存在，添加policy
            fcn = "AddPolicy";
            args.add(fileId);
            args.add("share");
            args.add(role);
            args.add(channelUsername);
        }
        Response response = fabricFeignService.invokeChaincodeTest(requester, peers, channelName, ccName, fcn, args);
        if(response.body().toString().contains("Success")){
            return true;
        }else{
            throw new FabricException("授权域间权限失败，info:" + response.body().toString());
        }
    }

    @Override
    public Boolean grantInnerChannelPush(String fileId, String role, String username) {
        return grantInnerChannelPull(fileId,role,username);
    }

    @Override
    public Boolean revokeInnerChannelPull(String fileId, String role, String username) {
        String requester = "org1_admin";
        String peers = getPeers(requester);
        String channelUsername = getChannelUsername(username);
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "UpdatePolicy";
        List<String> args = new ArrayList<String>();
        args.add(fileId);
        args.add("share");
        args.add("deleteuser");
        args.add(role);
        args.add(channelUsername);
        Response response = fabricFeignService.invokeChaincodeTest(requester, peers, channelName, ccName, fcn, args);
        if(response.body().toString().contains("Success")){
            return true;
        }else{
            throw new FabricException("授权域间权限失败，info:" + response.body().toString());
        }
    }

    @Override
    public Boolean revokeInnerChannelPush(String fileId, String role, String username) {
        return revokeInnerChannelPull(fileId, role, username);
    }


    public String getPeers(String requester) {
        String str1 = "peer0.org", str2 = ".example.com";
        char n = requester.charAt(3);
        return str1 + n + str2;
    }

    public String getChannelUsername(String requester) {
        String[] strings = requester.split("_");
        String prefix;
        if (strings[1].equals("admin")) {
            prefix = "Admin";
        } else {
            prefix = "User1";
        }
        char n = requester.charAt(3);
        return prefix + "@org" + n + ".example.com";
    }
    
    
}
