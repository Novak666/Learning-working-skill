# 1. Docker介绍

## 1.1 简介

Docker的思想来自于集装箱，集装箱解决了什么问题？在一艘大船上，可以把货物规整的摆放起来。并且各种各样的货物被集装箱标准化了，集装箱和集装箱之间不会互相影响。这样就可以不用单独使用其他的运输工具。大家都用一个标准，搬运集装箱了

1. 不同的应用程序可能会有不同的应用环境，有些软件安装之后会有端口之间的冲突，这时候，可以使用虚拟机来实现隔离，但是使用虚拟机的成本太高，而且消耗硬件
2. 不同的软件的环境都不一样，比如你用的是ubuntu，里面有个数据库，现在要迁移到centos中，但是此时需要从新在centos安装数据库，如果版本不一致，或者不支持，就会出现问题。比较麻烦。有了docker之后就不用这么麻烦了，直接将开发环境 搬运到不同的环境即可
3. 在服务器负载方面，如果你单独开一个虚拟机，那么虚拟机会占用空闲内存的，docker部署的话，这些内存就会利用起来

Docker是一个开源的应用容器引擎，基于Go语言开发。Docker可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的Linux机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口(类似iPhone的app)，更重要的是容器性能开销极低

Docker应用场景：

+ Web应用的自动化打包和发布
+ 自动化测试和持续集成、发布
+ 在服务型环境中部署和调整数据库或其他的后台应用

使用Docker可以实现开发人员的开发环境、测试人员的测试环境、运维人员的生产环境的一致性

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.08/pics/1.png)

## 1.2 Docker与虚拟机比较

Docker容器是在操作系统层面上实现虚拟化，直接复用本地主机的操作系统，而传统虚拟机则是在硬件层面实现虚拟化。与传统的虚拟机相比，Docker优势体现为启动速度快、占用体积小

## 1.3 Docker组成部分

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.08/pics/2.png)

+ 镜像：Docker镜像是用于创建Docker容器的模板。镜像是基于联合文件系统的一种层式结构，由一系列指令一步一步构建出来
+ 容器：Docker容器是独立运行的一个或一组应用。(镜像相当于类，容器相当于类的实例)
+ 客户端：Docker客户端通过命令行或者其他工具使用Docker API与Docker的守护进程通信
+ 主机：一个物理或者虚拟的机器用于执行Docker守护进程和容器
+ 守护进程：Docker服务器端进程，负责支撑Docker容器的运行以及镜像的管理
+ 仓库：Docker仓库用来保存镜像，可以理解为代码控制中的代码仓库。Docker Hub提供了庞大的镜像集合供使用。用户也可以将自己本地的镜像推送到Docker仓库供其他人下载

# 2. Docker安装与启动

## 2.1 Docker安装

以下是在CentOS7中安装Docker的步骤

1. yum包更新到最新

```shell
sudo yum update
```

2. 安装需要的软件包，yum-util提供yum-config-manager功能，另外两个是devicemapper驱动依赖的

```shell
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

3. 设置yum源

方案一：使用ustc的(推荐)

```shell
sudo yum-config-manager --add-repo http://mirrors.ustc.edu.cn/docker-ce/linux/centos/docker-ce.repo
```

方案二：使用阿里云(可能失败)

```shell
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

4. 安装docker，出现输入的界面都按y

```shell
sudo yum install -y docker-ce
```

5. 查看docker版本

```shell
docker -v
```

## 2.2 设置ustc镜像

https://lug.ustc.edu.cn/wiki/mirrors/help/docker

1. 编辑文件/etc/docker/daemon.json

```shell
#执行如下命令
mkdir /etc/docker
vi /etc/docker/daemon.json
```

2. 在文件中加入下面内容

```json
{
"registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
}
```

## 2.3 Docker启动与停止命令

```shell
#启动docker服务
systemctl start docker
#停止docker服务
systemctl stop docker
#重启docker服务
systemctl restart docker
#查看docker服务状态
systemctl status docker
#设置开机启动docker服务
systemctl enable docker
#查看docker概要信息
docker info
#查看docker帮助文档
docker --help
```

# 3. Docker镜像

Docker镜像是由文件系统叠加而成(是一种文件的存储形式)，是docker中的核心概念，可以认为镜像就是对某些运行环境或者软件打的包，用户可以从docker仓库中下载基础镜像到本地，比如开发人员可以从docker仓库拉取(下载)一个只包含centos7系统的基础镜像，然后在这个镜像中安装jdk、mysql、Tomcat和自己开发的应用，最后将这些环境打成一个新的镜像。开发人员将这个新的镜像提交给测试人员进行测试，测试人员只需要在测试环境下运行这个镜像就可以了，这样就可以保证开发人员的环境和测试人员的环境完全一致

## 3.1 查看镜像

```shell
docker images
```

## 3.2 搜索镜像

```shell
docker search 镜像名称
```

## 3.3 拉取镜像

```shell
#拉取镜像就是从Docker仓库下载镜像到本地，镜像名称格式为名称:版本号，如果版本号不指定则是最新的版本
docker pull 镜像名称
#如拉取centos7
docker pull centos:7
```

## 3.4 删除镜像

```shell
#可以按照镜像id删除镜像，命令如下
docker rmi $IMAGE_ID
#删除所有镜像
docker rmi `docker images -q`
```

# 4. Docker容器

容器，也是docker中的核心概念，容器是由镜像运行产生的运行实例。镜像和容器的关系，就如同Java语言中类和对象的关系

## 4.1 查看容器

```shell
#查看正在运行的容器
docker ps
#查看所有容器
docker ps -a
```

## 4.2 创建与启动容器

命令：docker run

参数说明

+ -i：表示运行容器
+ -t：表示容器启动后会进入其命令行。加入这两个参数后，容器创建就能登录进去。即分配一个伪终端
+ --name：为创建的容器命名
+ -v：表示目录映射关系(前者是宿主机目录，后者是映射到宿主机上的目录)，可以使用多个-v做多个目录或文件映射。注意：最好做目录映射，在宿主机上做修改，然后共享到容器上
+ -d：在run后面加上-d参数，则会创建一个守护式容器在后台运行(这样创建容器后不会自动登录容器，如果只加-i-t两个参数，创建后就会自动进去容器)
+ -p：表示端口映射，前者是宿主机端口，后者是容器内的映射端口。可以使用多个-p做多个端口映射

### 4.2.1 交互式容器

创建并启动一个交互式容器并取名为mycentos

```shell
docker run -it --name=mycentos centos:7 /bin/bash
```

开启另外一个终端来查看状态

```shell
docker ps
```

退出当前容器

```shell
exit
```

然后用docker ps -a命令查看发现该容器也随之停止

### 4.2.2 守护式容器

如果对于一个需要长期运行的容器来说，我们可以创建一个守护式容器，命令如下(容器名称不能重复)

```shell
docker run -di --name=mycentos2 centos:7
```

登录守护式容器(exit退出时，容器不会停止)

```shell
docker exec -it mycentos2 /bin/bash
```

## 4.3 启动与停止容器

启动已运行过的容器：docker start $CONTAINER_NAME/ID

```shell
docker start mycentos2
```

停止正在运行的容器：docker stop $CONTAINER_NAME/ID

```
docker stop mycentos2
```

## 4.4 文件拷贝

如果我们需要将文件拷贝到容器内(该命令一定要在宿主机执行)

```shell
docker cp 需要拷贝的文件或目录 容器名称:容器目录
```

也可以将文件从容器内拷贝出来

```shell
docker cp 容器名称:容器目录 需要拷贝的文件或目录
```

举例

```shell
#创建一个文件abc.txt
touch abc.txt
#复制abc.txt到mycentos2的容器的/目录下
docker cp abc.txt mycentos2:/
#进入mycentos2容器
docker exec -it mycentos2 /bin/bash
#查看容器/目录下文件
ll
```

## 4.5 目录挂载(映射)

我们可以在创建容器的时候，将宿主机的目录与容器内的目录进行映射，这样我们就可以通过修改宿主机某个目录的文件从而去影响容器里所对应的目录

```shell
#创建linux宿主机器要挂载的目录
mkdir /usr/local/test
#创建容器 添加-v参数 后边为宿主机目录:容器目录
docker run -di -v /usr/local/test:/usr/local/test --name=mycentos3 centos:7
#在linux下创建文件
touch /usr/local/test/def.txt
#进入容器
docker exec -it mycentos3 /bin/bash
#在容器中查看目录中是否有对应文件def.txt
ll /usr/local/test
```

如果你共享的是多级的目录，可能会出现权限不足的提示。这是因为CentOS7中的安全模块selinux把权限禁掉了，我们需要添加参数--privileged=true来解决挂载的目录没有权限的问题

```shell
docker run -di --privileged=true -v /root/test:/usr/local/test --name=mycentos4 centos:7
```

## 4.6 查看容器IP地址

查看容器运行的各种数据

```shell
docker inspect mycentos2
```

直接执行下面的命令直接输出IP地址

```shell
docker inspect --format='{{.NetworkSettings.IPAddress}}' mycentos2
```

## 4.7 删除容器

删除指定的容器：这个命令只能删除已经关闭的容器，不能删除正在运行的容器

```shell
docker rm $CONTAINER_ID/NAME
```

删除所有的容器

```shell
docker rm `docker ps -a -q`
```

或者

```shell
[root@localhost ~]# docker rm $(docker ps -aq)
```

# 5. 部署MySQL

## 5.1 拉取镜像

```shell
#注意版本
docker pull mysql
```

## 5.2 创建容器

+ -p：代表端口映射，格式为宿主机映射端口:容器运行端口
+ -e：代表添加环境变量，MYSQL_ROOT_PASSWORD是root用户的登陆密码

```shell
docker run -di --name=pinyougou_mysql -p 33306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```

## 5.3 进入容器

启动容器

```shell
docker exec -it mysql5.7 /bin/bash
```

登录mysql

```shell
mysql -u root -p
```

授权允许远程登录

```mysql
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
```

## 5.4 远程登录

利用连接工具

# 6. 部署Tomcat

## 6.1 拉取镜像

```shell
docker pull tomcat:7-jre8
```

## 6.2 创建容器

```shell
docker run -di --name=mytomcat -p 9100:8080 tomcat:7-jre8
```

# 7. 部署Nginx

## 7.1 拉取镜像

```shell
docker pull nginx
```

## 7.2 创建容器

```shell
docker run -di --name=mynginx -p 80:80 nginx
```

# 8. 部署Redis

## 8.1 拉取镜像

```shell
docker pull redis
```

## 8.2 创建容器

```shell
docker run -di --name=myredis -p 6379:6379 redis
```

本地客户端进行测试

# 9. 备份与迁移

## 9.1 容器保存为镜像

```shell
#docker commit 容器名称 新镜像名称
docker commit mynginx mynginx
```

## 9.2 镜像备份

```shell
#docker save –o 文件名.tar 镜像名
docker save -o mynginx.tar mynginx
```

## 9.3 恢复镜像

```shell
#docker load -i tar文件名

#停止mynginx容器
docker stop mynginx
#删除mynginx容器
docker rm mynginx
#删除mynginx镜像
docker rmi mynginx
#加载恢复mynginx镜像
docker load -i mynginx.tar
#在镜像恢复之后，基于该镜像再次创建启动容器
docker run -di --name=mynginx -p 80:80 mynginx
```

