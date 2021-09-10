package AbeTest;

import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.utils.AbeUtil;
import com.hust.keyRD.system.utils.MongoDbUtil;
import com.hust.keyRD.system.utils.SerializeUtil;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import sg.edu.ntu.sce.sands.crypto.dcpabe.Ciphertext;
import sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters;
import sg.edu.ntu.sce.sands.crypto.dcpabe.Message;
import sg.edu.ntu.sce.sands.crypto.utility.Utility;

import java.io.*;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/9 11:05
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan("com.hust.keyRD.*")
public class AbeUtilTest {
    @Value("${dcpabe.filePath}")
    public static String rootPath;

    @Autowired
    public AbeUtil abeUtil;

    @Autowired
    public MongoDbUtil mongoDbUtil;


    @Test
    public void testInit() throws IOException, ClassNotFoundException {
        abeUtil.initGp();
        GlobalParameters gp1 =  AbeUtil.gp;
        AbeUtil.gp = null;
        abeUtil.initGp();
        System.out.println(gp1);
        System.out.println("----------------------------------------");
        System.out.println(AbeUtil.gp);
        System.out.println(gp1.equals(AbeUtil.gp));
    }

    // 测试声明全部属性
    @Test
    public void authoritySetUpTest() throws IOException {
        String[] attrs = {"a", "b", "c", "d"};
        abeUtil.authoritySetUp(attrs);
    }

    @Test
    public void grantAttrToUserTest(){
        String[] attrs = {"a", "d"};
        abeUtil.grantAttrToUser(attrs, "user1");
    }

    // 测试根据policy进行加密
    @Test
    public void encryptTest() throws IOException, InvalidCipherTextException, ClassNotFoundException {
        String policy = "and a or d and b c";
        String txt = "dpabe: hello world";
        ByteArrayOutputStream byteArrayOutputStream = abeUtil.encrypt(txt.getBytes("UTF-8"), policy);
        mongoDbUtil.upload(byteArrayOutputStream.toByteArray(), "file#enc");
    }

    @Test
    public void decrypt() throws IOException, ClassNotFoundException {
        FileModel ciphertext = mongoDbUtil.getByName("file#enc");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ciphertext.getContent().getData());

        String[] attrs = {"a", "d"};
        ByteArrayOutputStream outputStream = abeUtil.decrypt(byteArrayInputStream, "user1", attrs);
        System.out.println(new String(outputStream.toByteArray()));
    }

    @Test
    public void testPath(){
        System.out.println(rootPath);
    }
}
