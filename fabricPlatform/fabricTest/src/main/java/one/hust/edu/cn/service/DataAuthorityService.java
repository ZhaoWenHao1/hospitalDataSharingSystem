package one.hust.edu.cn.service;

import one.hust.edu.cn.entities.DataAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataAuthorityService {
    //添加文件权限
    void addDataAuthority(DataAuthority dataAuthority);
    //查找某一个文件的所有权限
    DataAuthority findDataAuthorityById(Integer id);
    //查找某一个用户的所有权限
    List<DataAuthority> findDataAuthorityByUserId(Integer userId);
    //查找某一个文件的所有权限
    List<DataAuthority> findDataAuthorityByDataId(Integer dataSampleId);
    //根据用户id和文件id查询权限
    List<DataAuthority> findDataAuthorityByUserIdAndDataId(Integer userId,Integer dataSampleId);
    //根据id更新文件权限
    void editDataAuthority(@Param("id")Integer id,@Param("authorityKey") Integer authorityKey);
    //查询是否有操作权限
    Integer checkDataAuthority(DataAuthority dataAuthority);
    //插入文件上传者的权限，包括修改，查看，删除
    void addMasterDataAuthority(Integer originUserId, Integer dataSampleId);
    //获取所有权限
    List<DataAuthority> getAllAuthority();
}
