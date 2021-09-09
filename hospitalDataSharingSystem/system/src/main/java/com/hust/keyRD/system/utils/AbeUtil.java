package com.hust.keyRD.system.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import com.hust.keyRD.system.file.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import sg.edu.ntu.sce.sands.crypto.dcpabe.*;
import sg.edu.ntu.sce.sands.crypto.dcpabe.ac.AccessStructure;
import sg.edu.ntu.sce.sands.crypto.utility.Utility;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/9 10:42
 **/
@Component
public class AbeUtil {

    @Autowired
    public MongoDbUtil mongoDbUtil;

    private final String gpName = "dcpabe.gp";
    private final String akSecKeyName = "akSecKey";
    private final String akPubKeyName = "akPubKey";

//    @Value("${dcpabe.filePath}")
//    @Value("classpath:/abe/")
    public static String rootPath;

    public static GlobalParameters gp;
    private static PublicKeys publicKeys;
    private static String gpPath;
    private static String akSecKeyPath;
    private static String akPubKeyPath;

//    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        rootPath = AbeUtil.getRootPath();
        gpPath = rootPath + "abe/dcpabe.gp";
        akSecKeyPath = rootPath + "abe/akSecKey";
        akPubKeyPath = rootPath + "abe/akPubKey";
        initGp();
    }


    public AuthorityKeys authoritySetUp(String[] attrs) throws IOException {
        AuthorityKeys ak = DCPABE.authoritySetup("authority", gp, attrs);

        Utility.writeSecretKeys(akSecKeyPath, ak.getSecretKeys());
        Utility.writePublicKeys(akPubKeyPath, ak.getPublicKeys());

        publicKeys.subscribeAuthority(ak.getPublicKeys());
        // todo upload到 monogoDb
        return ak;
    }

//    public void encrypt(byte[] data, String policy){
//        Message message = new Message(data);
//        AccessStructure as = AccessStructure.buildFromPolicy(policy);
//        DCPABE.encrypt(message,)
//    }


    public static String getRootPath(){
        try{
            return ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e){
            return "";
        }
    }

    public void initGp() throws IOException, ClassNotFoundException {
        FileModel fileModel = mongoDbUtil.getByName("dcpabe.gp");
        if(fileModel == null){
            System.out.println("generate");
            gp = DCPABE.globalSetup(160);
            mongoDbUtil.upload(SerializeUtil.serialize(gp), "dcpabe.gp");
        }else {
            System.out.println("unSerialize");
            gp = (GlobalParameters) SerializeUtil.unserialize(fileModel.getContent().getData());
        }
//        rootPath = AbeUtil.getRootPath();
//        // 从本地读
//        // to do ： 从monogodb中读
//        File gpFile = new File(rootPath + "abe/dcpabe.gp");
//        if(gpFile.exists()){
//            gp = Utility.readGlobalParameters(gpFile.getPath());
//        }else {
//            gp = DCPABE.globalSetup(160);
//
//            Utility.writeGlobalParameters(gpFile.getPath(), gp);
//        }
    }

    public static String generateNameByUserAndAttr(String username, String[] attrs){
        StringBuilder sb = new StringBuilder();
        sb.append("abeAttr#");
        sb.append(username);
        for (String attr : attrs) {
            sb.append("#");
            sb.append(attr);
        }
        return sb.toString();
    }
}
