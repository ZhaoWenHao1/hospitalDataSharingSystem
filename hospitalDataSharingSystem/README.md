## 项目说明

目前最新代码在分支 **zwh2**

## 运行步骤

1.maven取消test步骤进行打包（该过程中会下载依赖）

<img src="https://i.loli.net/2021/07/02/6lUmx4fFWrZsEoD.png" alt="image-20210702203443405" style="zoom:67%;" />

2. 运行system模块中的HospitalMain类启动后台

   <img src="https://i.loli.net/2021/07/02/GO6KPs2ewN1q7Ia.png" alt="image-20210702203536192" style="zoom:67%;" />

一些说明：

- 调用django服务的说明

-![image-20210702203741968](https://i.loli.net/2021/07/02/trfOvkdQLYljBXU.png)

- FabricService接口说明

  <img src="https://i.loli.net/2021/07/02/awPWl3Db16tGsoi.png" alt="image-20210702203844786" style="zoom: 67%;" />

  在目前你们fabric版本中只有用户相关能成功调用了fabric接口，文件权限相关的（uncheck的）有bug，但系统都能正常跑，我们自己实现了一套逻辑。

- 现在系统连接的服务有 55服务器上的 mysql，mongoDB，
  Redis，53服务器上的django，所以系统运行需要在校园网内。详细配置在hospitalDataSharingSystem/system/src/main/resources/application.yml

  <img src="https://i.loli.net/2021/07/02/P6WJCmnD2oQYtcI.png" alt="image-20210702204726392" style="zoom:67%;" />

  服务运行的端口设置用port配置，假设服务运行所在电脑为ip，那ip:port 需要与前端中request.js文件中的配置一致，才能前后端连通。

  ![image-20210702204853252](https://i.loli.net/2021/07/02/FemMagTirvL7n39.png)

后端与django的交互地址在这个地方定义，下图方框内为django服务所提供ip和端口，这个配置正确才能后端与django连通

<img src="C:\Users\14287\AppData\Roaming\Typora\typora-user-images\image-20210702205030439.png" alt="image-20210702205030439" style="zoom:80%;" />

