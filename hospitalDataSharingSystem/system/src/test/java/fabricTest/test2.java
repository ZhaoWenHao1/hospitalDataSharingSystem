package fabricTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class test2 {
    public static void main(String[] args1) {
        int a = 18;
        int b = 23;
        try {
            String[] args = new String[] { "python", "D:\\研究生资料\\南六218实验室\\代炜琦项目组文件\\github同步代码\\python代码\\demo2.py", String.valueOf(a), String.valueOf(b) };
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            //getRuntime().exec(args)方法从main函数开始走，python中sys.argv[i]从第三个参数开始
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
