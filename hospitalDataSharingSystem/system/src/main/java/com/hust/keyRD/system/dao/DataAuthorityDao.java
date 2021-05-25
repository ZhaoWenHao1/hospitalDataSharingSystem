package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.Dto.UserDataAuthDto;
import com.hust.keyRD.commons.entities.DataAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface DataAuthorityDao {
    //添加文件权限
    void addDataAuthority(DataAuthority dataAuthority);

    //给用户，文件撤销权限
    Integer deleteDataAuthority(DataAuthority dataAuthority);

    //查找某一个文件的所有权限
    DataAuthority findDataAuthorityById(Integer id);

    //查找某一个用户的所有权限
    List<DataAuthority> findDataAuthorityByUserId(Integer userId);

    //查找某一个文件的所有权限
    List<DataAuthority> findDataAuthorityByDataId(Integer dataSampleId);


    //根据用户id和文件id查询权限
    List<DataAuthority> findDataAuthorityByUserIdAndDataId(@Param("userId") Integer userId, @Param("dataSampleId") Integer dataSampleId);

    //根据id更新文件权限
    void editDataAuthority(@Param("id") Integer id, @Param("authorityKey") Integer authorityKey);

    //查询是否有操作权限
    Integer checkDataAuthority(DataAuthority dataAuthority);

    //插入文件上传者的权限，包括修改，查看，删除
    void addMasterDataAuthority(@Param("userId") Integer userId, @Param("dataSampleId") Integer dataSampleId);

    //获取所有权限
    List<DataAuthority> getAllAuthority();

    // 获取除管理员外的所有用户对所有文件的权限
    List<UserDataAuthDto> findUsersDataAuthority();
}
