# hospitalDataSharingSystem
基于fabric的医疗数据共享平台
## Swagger
### 注解说明
- @Api：用于修饰Controller类，生成Controller相关文档信息
- @ApiOperation：用于修饰Controller类中的方法，生成接口方法相关文档信息
- @ApiParam：用于修饰接口中的参数，生成接口参数相关文档信息
- @ApiModelProperty：用于修饰实体类的属性，当实体类是请求参数或返回结果时，直接生成相关文档信息

 
## MapStruct映射工具

## mongoDB

## feign

# 更新说明2021.01.15
## 需求：文件需要在任一channel上
设计： 原obj为fileId  改为  fileId#channelName  
具体的，python server不改，只改后端传入python server的fileId为 fileId#channelName
由于第一次上链是进行权限审计，所以传入的fileHash暂时全为0

## 文件共享read权限
## 所有用户对所有文件有溯源权限
## channel的划分
