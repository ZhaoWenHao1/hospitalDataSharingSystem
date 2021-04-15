# 开发环境部署
## mysql mongoDB
启动docker 该镜像里部署好了mysql(3307 username:dev pwd:dev) 和 mongoDB(27018 username:root, pwd:fabric)
```shell
docker run --name mysql -v /var/docker_data/mysql/data/:/var/lib/mysql -d -p 3307:3306 -p 5005:5005 -p 5001:22 happyzhao1010/mysql
docker run --name mysqlDev -v /var/docker_data/mysql/data/:/var/lib/mysql -d -p 3307:3306 -p 27018:27017 -p 5005:5005 -p 5001:22 happyzhao1010/mysql
```
进入容器
```shell
docker exec -it mysql bash
```
开启容器ssh服务，以便直接ssh进入容器
```shell
service ssh start
```
开启mongoDB
```shell
/usr/local/mongoDB/bin/mongod --config /usr/local/mongoDB/mongodb.conf
```
