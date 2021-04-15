package com.hust.keyRD.system.file.service;

import com.hust.keyRD.system.file.model.FileModel;

import java.util.List;
import java.util.Optional;

/**
 * @program: system
 * @description: FileService
 * @author: zwh
 * @create: 2021-01-14 16:53
 **/
public interface FileService {
    /**
     * 保存文件
     */
    FileModel saveFile(FileModel file);

    /**
     * 删除文件
     */
    void removeFile(String id);

    /**
     * 根据id获取文件
     */
    Optional<FileModel> getFileById(String id);

    /**
     * 分页查询，按上传时间降序
     * @return
     */
    List<FileModel> listFilesByPage(int pageIndex, int pageSize);

    /**
     * 复制文件 
     * @param file
     * @return
     */
    FileModel copyFile(FileModel file);
}
