package com.hust.keyRD.system.utils;

import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.service.ApplyService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import sg.edu.ntu.sce.sands.crypto.DCPABETool;
import sg.edu.ntu.sce.sands.crypto.dcpabe.*;
import sg.edu.ntu.sce.sands.crypto.dcpabe.ac.AccessStructure;
import sg.edu.ntu.sce.sands.crypto.dcpabe.key.PersonalKey;
import sg.edu.ntu.sce.sands.crypto.dcpabe.key.PublicKey;
import sg.edu.ntu.sce.sands.crypto.dcpabe.key.SecretKey;
import sg.edu.ntu.sce.sands.crypto.utility.Utility;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/9 10:42
 **/
@Component
@Slf4j
public class AbeUtil {

    @Autowired
    public MongoDbUtil mongoDbUtil;

    @Autowired
    public ApplyService applyService;

    private final String gpName = "dcpabe.gp";
    private final String akSecKeyName = "akSecKey";
    private final String akPubKeyName = "akPubKey";

    public static volatile GlobalParameters gp;
    private static volatile Map<String, PublicKey> publicKeys;
    private static volatile Map<String, SecretKey> secretKeys;


    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        initGp();
        initPublicKeys();
        initPublicKeys();
    }


    /**
     * 声明全部属性  并将全部属性声明后的公私钥序列化上传到monogo
     *
     * @param attrs 全部属性
     * @return
     * @throws IOException
     */
    public AuthorityKeys authoritySetUp(String[] attrs) throws IOException {
        if (gp == null) {
            initGp();
        }

        AuthorityKeys ak = DCPABE.authoritySetup("authority", gp, attrs);

        mongoDbUtil.upload(Objects.requireNonNull(SerializeUtil.serialize(ak.getPublicKeys())), akPubKeyName);
        mongoDbUtil.upload(Objects.requireNonNull(SerializeUtil.serialize(ak.getSecretKeys())), akSecKeyName);

        publicKeys = ak.getPublicKeys();
        secretKeys = ak.getSecretKeys();
        return ak;
    }

    /**
     * 将数据根据policy进行加密
     *
     * @param data   要加密的数据
     * @param policy 加密策略
     * @return
     */
    public ByteArrayOutputStream encrypt(byte[] data, String policy) throws IOException, InvalidCipherTextException {
        AccessStructure as = AccessStructure.buildFromPolicy(policy);
        if (gp == null) {
            initGp();
        }
        if (publicKeys == null) {
            initPublicKeys();
        }
        PublicKeys pKeys = new PublicKeys();
        pKeys.subscribeAuthority(publicKeys);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        ByteArrayInputStream byteIs = new ByteArrayInputStream(data);
        BufferedInputStream bis = new BufferedInputStream(byteIs);

        Message m = DCPABE.generateRandomMessage(gp);
        Ciphertext ct = DCPABE.encrypt(m, as, gp, pKeys);

        oos.writeObject(ct);
        PaddedBufferedBlockCipher aes = Utility.initializeAES(m.getM(), true);
        DCPABETool.encryptOrDecryptPayload(aes, bis, oos);
        oos.flush();
        return bos;
    }

    /**
     * 授权用户属性
     *
     * @param attrs
     * @param username
     */
    public void grantAttrToUser(String[] attrs, String username) {
        if (gp == null) {
            initGp();
        }
        if (secretKeys == null) {
            initSecretKeys();
        }

        for (String attr : attrs) {
            PersonalKey personalKey = DCPABE.keyGen(username, attr, secretKeys.get(attr), gp);
            String fileName = generateNameByUserAndAttr(username, attr);
            mongoDbUtil.upload(Objects.requireNonNull(SerializeUtil.serialize(personalKey)), fileName);
        }
    }

    /**
     * 文件解密
     * @param bis
     * @param username
     * @param attrs
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ByteArrayOutputStream decrypt(ByteArrayInputStream bis, String username, String[] attrs) throws IOException, ClassNotFoundException {
        if (gp == null) {
            initGp();
        }
        PersonalKeys personalKeys = new PersonalKeys(username);
        for (String attr : attrs) {
            String fileName = generateNameByUserAndAttr(username, attr);
            FileModel fileModel = mongoDbUtil.getByName(fileName);
            if (fileModel != null) {
                PersonalKey pk = (PersonalKey) SerializeUtil.unserialize(fileModel.getContent().getData());
                personalKeys.addKey(pk);
            } else {
                log.warn(String.format("decrypt error: attr[{}] did not be granted", attr));
                throw new RuntimeException(String.format("decrypt error: attr[{}] did not be granted", attr));
            }
        }

        ObjectInputStream oIn = new ObjectInputStream(bis);
        Ciphertext ct = Utility.readCiphertext(oIn);
        Message message = DCPABE.decrypt(ct, personalKeys, gp);
        PaddedBufferedBlockCipher aes = Utility.initializeAES(message.getM(), false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BufferedOutputStream bos = new BufferedOutputStream(byteArrayOutputStream)) {
            DCPABETool.encryptOrDecryptPayload(aes, oIn, bos);
            bos.flush();
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream;
    }

    /**
     * 检查是否能够解密
     * @param policy
     * @param attrs
     * @return
     */
    public boolean checkIfHaveAttr(String policy, String[] attrs){
        AccessStructure accessStructure = AccessStructure.buildFromPolicy(policy);
        return accessStructure.getIndexesList(Arrays.stream(attrs).collect(Collectors.toList())) != null;
    }


    /**
     * 初始化全局gp  如果mongoDb中有，则反序列化，否则生成并上传到mongoDB
     */
    public void initGp() {
        if (gp == null) {
            synchronized (AbeUtil.class) {
                if (gp == null) {
                    try {
                        FileModel fileModel = mongoDbUtil.getByName(gpName);
                        if (fileModel == null) {
                            log.info("generate GlobalParameters");
                            gp = DCPABE.globalSetup(160);
                            mongoDbUtil.upload(Objects.requireNonNull(SerializeUtil.serialize(gp)), "dcpabe.gp");
                        } else {
                            log.info("unSerialize GlobalParameters");
                            gp = (GlobalParameters) SerializeUtil.unserialize(fileModel.getContent().getData());
                        }
                    } catch (Exception e) {
                        log.warn("gp init error: " + e.getMessage());
                    }
                }
            }
        }
    }

    public void initPublicKeys() {
        if (publicKeys == null) {
            synchronized (AbeUtil.class) {
                if (publicKeys == null) {
                    FileModel fileModel = mongoDbUtil.getByName(akPubKeyName);
                    if (fileModel != null) {
                        log.info("unSerialize PublicKeys");
                        publicKeys = (Map<String, PublicKey>) SerializeUtil.unserialize(fileModel.getContent().getData());
                    } else {
                        log.warn("PublicKeys init error: file is not exist");
                    }
                }
            }
        }
    }

    public void initSecretKeys() {
        if (secretKeys == null) {
            synchronized (AbeUtil.class) {
                if (secretKeys == null) {
                    FileModel fileModel = mongoDbUtil.getByName(akSecKeyName);
                    if (fileModel != null) {
                        log.info("unSerialize SecretKeys");
                        secretKeys = (Map<String, SecretKey>) SerializeUtil.unserialize(fileModel.getContent().getData());
                    } else {
                        log.warn("SecretKeys init error: file is not exist");
                    }
                }
            }
        }
    }



    public static String generateNameByUserAndAttr(String username, String[] attrs) {
        StringBuilder sb = new StringBuilder();
        sb.append("abeAttr#");
        sb.append(username);
        for (String attr : attrs) {
            sb.append("#");
            sb.append(attr);
        }
        return sb.toString();
    }

    public static String generateNameByUserAndAttr(String username, String attr) {
        StringBuilder sb = new StringBuilder();
        sb.append("abeAttr#");
        sb.append(username);
        sb.append("#");
        sb.append(attr);
        return sb.toString();
    }
}
