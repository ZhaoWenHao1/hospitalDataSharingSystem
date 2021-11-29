package AbeTest;

import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.vo.AttributesVO;
import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.init.SystemInit;
import com.hust.keyRD.system.service.ApplyService;
import com.hust.keyRD.system.service.UserService;
import com.hust.keyRD.system.utils.AbeUtil;
import com.hust.keyRD.system.utils.MongoDbUtil;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters;
import sg.edu.ntu.sce.sands.crypto.dcpabe.ac.AccessStructure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    public SystemInit systemInit;

    @Autowired
    public ApplyService applyService;

    @Autowired
    public UserService userService;

    @Test
    public void systemInit() throws IOException {
        systemInit.abeInit();
//        systemInit.grantAttrToUser();
    }


    @Test
    public void testInit() throws IOException, ClassNotFoundException {
        abeUtil.initGp();
        GlobalParameters gp1 = AbeUtil.gp;
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
//        String[] attrs = {"a", "b", "c", "d"};
        List<AttributesVO> attributesList = applyService.getAttributesList();
        List<String> list = new ArrayList<>();
        for (AttributesVO attributesVO : attributesList) {
            attributesVO.getValue().stream().forEach(val ->{
                list.add(attributesVO.getKey() + ":" + val);
            });
        }
        System.out.println(list);
        String[] attrs = list.toArray(new String[list.size()]);
        abeUtil.authoritySetUp(attrs);
    }

    @Test
    public void grantAttrToUserTest() {
//        String[] attrs = {"a", "d"};
        List<User> allUser = userService.getAllUserAndAdmin();
        for (User user : allUser) {
            if(StringUtils.isBlank(user.getAttributes())){
                continue;
            }
            String[] attrs = user.getAttributes().split(",");
            for (String attr : attrs) {
                System.out.print(attr + " ");
            }
            System.out.println();
//            abeUtil.grantAttrToUser(attrs, user.getUsername());
        }
    }

    @Test
    public void grantAttrToUserTest2() {
        String[] attrs = {"city:GuangDong", "city:HuNan"};
        abeUtil.grantAttrToUser(attrs, "user2");
    }

    // 测试根据policy进行加密
    @Test
    public void encryptTest() throws IOException, InvalidCipherTextException, ClassNotFoundException {
        String policy = "and a or d and b c";
        policy = "a and (d or (b and c))";
        String txt = "dpabe: hello world";
        ByteArrayOutputStream byteArrayOutputStream = abeUtil.encrypt(txt.getBytes(), policy);
        mongoDbUtil.upload(byteArrayOutputStream.toByteArray(), "file#enc");
    }

    @Test
    public void decrypt() throws IOException, ClassNotFoundException {
        FileModel ciphertext = mongoDbUtil.getByName("file#enc");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ciphertext.getContent().getData());

        String[] attrs = {"a", "b"};
        ByteArrayOutputStream outputStream = abeUtil.decrypt(byteArrayInputStream, "user2", attrs);
        System.out.println(new String(outputStream.toByteArray()));
    }

    @Test
    public void enAndDecryptTest() throws InvalidCipherTextException, IOException, ClassNotFoundException {
        encryptTest();
        decrypt();
    }

    @Test
    public void policyBuildTest() throws InvalidCipherTextException, IOException {
        AccessStructure accessStructure1 = AccessStructure.buildFromPolicy("and a or d and b c");
        AccessStructure accessStructure2 = AccessStructure.buildFromPolicy("a and (d or (b and c))");
        System.out.println(accessStructure1);
        System.out.println(accessStructure2);
        String txt = "dpabe: hello world";
        ByteArrayOutputStream byteArrayOutputStream1 = abeUtil.encrypt(txt.getBytes(), "and a or d and b c");
        ByteArrayOutputStream byteArrayOutputStream2 = abeUtil.encrypt(txt.getBytes(), "a and ( d or ( b and c ) )");

        System.out.println(accessStructure1.equals(accessStructure2));

    }

    @Test
    public void decryptAttrTest(){
        String policy = "and a or d and b c";
        String[] attrs1 = {"a", "b"};
        String[] attrs2 = {"a", "d"};
        System.out.println(abeUtil.checkIfHaveAttr(policy, attrs1));
        System.out.println(abeUtil.checkIfHaveAttr(policy, attrs2));
    }



}
