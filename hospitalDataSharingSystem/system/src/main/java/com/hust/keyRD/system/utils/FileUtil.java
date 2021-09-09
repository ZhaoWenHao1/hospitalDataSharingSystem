package com.hust.keyRD.system.utils;

import java.io.File;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/9 11:00
 **/
public class FileUtil {
    public static boolean fileExists(String filePath){
        File file = new File(filePath);
        return file.exists();
    }
}
