package com.hust.keyRD.system.file.service.impl;

import com.hust.keyRD.commons.exception.mongoDB.MongoDBException;
import com.hust.keyRD.system.file.dao.FileRepository;
import com.hust.keyRD.system.file.model.FileModel;
import com.hust.keyRD.system.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @program: system
 * @description: FileServiceImpl
 * @author: zwh
 * @create: 2021-01-14 16:54
 **/
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileModel saveFile(FileModel file) {

        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.deleteById(id);
    }

    @Override
    public Optional<FileModel> getFileById(String id) {
        return fileRepository.findById(id);
    }

    @Override
    public Optional<FileModel> getFileByName(String fileName) {
        FileModel fileModel = new FileModel();
        fileModel.setName(fileName);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("size");
        Example<FileModel> example = Example.of(fileModel, matcher);
        Sort sort = Sort.by(Sort.Direction.DESC, "uploadDate");
        List<FileModel> fileModels = fileRepository.findAll(example, sort);
        if(fileModels.size() > 0){
            return  Optional.of(fileModels.get(0));
        }else {
            return Optional.empty();
        }
    }

    @Override
    public List<FileModel> listFilesByPage(int pageIndex, int pageSize) {
        Page<FileModel> page = null;
        List<FileModel> list = null;
        Sort sort = Sort.by(Sort.Direction.DESC,"uploadDate");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        page = fileRepository.findAll(pageable);
        list = page.getContent();
        return list;
    }

    @Override
    public FileModel copyFile(FileModel file) {
        FileModel newFileModel = new FileModel(file.getName(),file.getContentType(),file.getSize(),file.getContent());
        newFileModel.setUploadDate(new Date());
        newFileModel.setMd5(file.getMd5());
        newFileModel.setPath(file.getPath());
        saveFile(newFileModel);//将新文件进行保存
        return newFileModel;
    }
}
