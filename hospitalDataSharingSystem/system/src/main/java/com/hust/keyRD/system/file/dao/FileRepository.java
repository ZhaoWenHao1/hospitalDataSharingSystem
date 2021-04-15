package com.hust.keyRD.system.file.dao;

import com.hust.keyRD.system.file.model.FileModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @program: system
 * @description: file 持久层
 * @author: zwh
 * @create: 2021-01-14 16:33
 **/
public interface FileRepository extends MongoRepository<FileModel,String> {
//    public updateContent()
}
