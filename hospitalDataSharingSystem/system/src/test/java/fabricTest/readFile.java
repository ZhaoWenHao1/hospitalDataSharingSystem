package fabricTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
 
 
public class readFile {
	private static ArrayList<String> listname = new ArrayList<String>();
	public static void main(String[] args)throws Exception{
		readAllFile("D:\\研究生资料\\南六218实验室\\代炜琦项目组文件\\github同步代码\\uploadFilePackage\\");
		System.out.println(listname.size());
	}
	public static void readAllFile(String filepath) {
		File file= new File(filepath);
		if(!file.isDirectory()){
			listname.add(file.getName());
		}else if(file.isDirectory()){
			System.out.println("文件");
			String[] filelist=file.list();
			for(int i = 0;i<filelist.length;i++){
				File readfile = new File(filepath);
				if (!readfile.isDirectory()) {
                    listname.add(readfile.getName());
				} else if (readfile.isDirectory()) {
					readAllFile(filepath + "\\" + filelist[i]);//递归
				}
			}
		}
		for(int i = 0;i<listname.size();i++){
			System.out.println(listname.get(i));
		}
	}
}