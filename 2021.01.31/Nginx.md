# Nginx

## 1.1 概述

Nginx是一种服务器软件，其最主要，最基本的功能是可以与服务器硬件(电脑)结合，让程序员可以将程序发布在Nginx服务器上，让成千上万的用户可以浏览。

​	除此之外，Nginx还是一种高性能的HTTP和反向代理服务器，同时也是一个代理邮件服务器。也就是说，我们在Nginx上可以： 

1. 可以发布网站(静态, html,css,js)
2. 可以实现负载均衡, 
3. 代理服务器
4. 可以作为邮件服务器实现收发邮件等功能

本课程我们只讨论Nginx发布网站的功能，其它的功能后续课程会深入学习.

## 1.2 配置安装Nginx

### 1.2.1 上传安装包

打开CRT，按Alt+p

```
put 目录/nginx.tar.gz
```

### 1.2.2 解压安装包

```
tar -zxvf nginx.tar.gz
```

### 1.2.3 进入Nginx目录

### 1.2.3 进入Nginx目录

```
cd nginx
```

### 1.2.4 安装依赖环境
```
yum -y install pcre pcre-devel
yum -y install zlib zlib-devel
yum -y install openssl openssl-devel
```

### 1.2.5 安装Nginx
```
./configure
make
make install
```


安装后在/usr/local下就会有一个nginx目录

### 1.2.6 启动Nginx
```
cd /usr/local/nginx/sbin
启动
./nginx
停止
./nginx -s stop
重启
./nginx -s reload
```

### 1.2.7 查看服务状态
```
ps -ef | grep nginx
```

### 1.2.8 测试Nginx服务是否成功启动
```
http://ip地址:80
```

## 1.3 发布项目

### 1.3.1 创建一个toutiao目录
```
cd /home
mkdir toutiao
```

### 1.3.2 将项目上传到toutiao目录

```
put 目录/web.zip
mv web.zip /home/toutiao
```

### 1.3.3 解压项目
```
unzip web.zip
```

### 1.3.4 编辑Nginx配置文件nginx-1.17.5/conf/nginx.conf
```
server {
	listen       80;
	server_name  localhost;

#charset koi8-r;

#access_log  logs/host.access.log  main;

location / {
	root   /home/toutiao; //修改一下
	index  index.html index.htm;
}
```

### 1.3.5 关闭nginx服务
```
./nginx -s stop
```

### 1.3.6 启动服务并加载配置文件
```
/usr/local/nginx/sbin/nginx -c /home/nginx-1.17.5/conf/nginx.conf
```

### 1.3.7 浏览器打开网址

```
http://ip地址
```

