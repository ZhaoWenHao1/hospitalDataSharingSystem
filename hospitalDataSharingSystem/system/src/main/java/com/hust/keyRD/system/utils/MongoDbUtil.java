package com.hust.keyRD.system.utils;

import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.file.service.FileService;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/9 15:38
 **/
@Component
public class MongoDbUtil {

    @Autowired
    public FileService fileService;

    private Map<String, FileModel> fileCache = new ConcurrentHashMap<>();

    public FileModel getByName(String fileName){
        if(fileCache.containsKey(fileName)){
            return fileCache.get(fileName);
        }
        Optional<FileModel> fileByName = fileService.getFileByName(fileName);
        FileModel fileModel = fileByName.orElse(null);
        if(fileModel != null && !fileCache.containsKey(fileName)){
            fileCache.put(fileName, fileModel);
        }
        return fileModel;
    }

    public void upload(byte[] bytes, String fileName){
        FileModel fileModel = new FileModel(fileName, null, bytes.length, new Binary(bytes));
        fileService.saveFile(fileModel);
        fileCache.put(fileName, fileModel);
    }
}
