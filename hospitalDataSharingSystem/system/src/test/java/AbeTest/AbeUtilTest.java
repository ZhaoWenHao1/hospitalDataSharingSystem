package AbeTest;

import com.hust.keyRD.system.utils.AbeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters;

import java.io.IOException;

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

    @Test
    public void testPath(){
        System.out.println(rootPath);
    }
}
