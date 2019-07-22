Sparrow项目快速部署

> 作者：陈绿佳，郑铠锋，梁宏达，陈晓滨，李培文

[TOC]

## 1. 环境部署&数据准备
### 1.1 ES

### 1.2 PostgreSQL
#### 1.2.1 环境部署
##### 安装PostgresSQL

```shell
# 下载rpm包
[root@demo ~]# yum install https://download.postgresql.org/pub/repos/yum/11/redhat/rhel-7-x86_64/pgdg-redhat11-11-2.noarch.rpm -y

# 安装
[root@demo ~]# yum -y install postgresql11 postgresql11-server postgresql11-libs

# 初始化数据库
[root@demo ~]# /usr/pgsql-11/bin/postgresql-11-setup initdb

# 设置开机自启动PostgreSQL和启动服务
[root@demo ~]# systemctl enable postgresql-11
[root@demo ~]# systemctl start postgresql-11
[root@demo ~]# systemctl status postgresql-11

## 切换用户后进入psql
[root@demo ~]# su - postgres
[root@demo ~]# psql
## 修改密码
alter user postgres password '密码'
```

##### 允许远程连接

```shell
# 修改配置文件postgresql.conf
[root@demo ~]# vi /var/lib/pgsql/11/data/postgresql.conf
#找到listen_address那里，解开注释并修改引号内localhost的值为*
listen_address="*"

# 修改配置文件pg_hba.conf
[root@demo ~]# vi /var/libg/pgsql/11/data/pg_hba.conf
#在文件最后加上下面这行代码
host all all 0.0.0.0/0 trust

# 重启PostgreSQL服务
[root@demo ~]# systemctl restart postgresql-11
```
#### 1.2.2 数据准备
方式：SQL文件导入("dum.sql“)，其中插入了项目运行必要的数据（root、home目录等）。

数据导入之前，请先在PostgreSQL中新建数据库

```shell
create database sparrow
```

数据导入

```shell
[root@demo ~]# psql -U posrgres(用户名) 数据库名 < ./sparrow.sql
```

### 1.3 Redis
#### 1.3.1 环境部署
##### 安装Redis
```shell
# 下载Redis安装包
[root@demo ~]# wget http://download.redis.io/releases/redis-5.0.5.tar.gz

# 解压压缩包
[root@demo ~]# tar -zxvf redis-5.0.5.tar.gz

# 检查服务器是否有gcc依赖
[root@demo ~]# gcc -v
# 若没有gcc依赖，则安装
[root@demo ~]# yum insall gcc

# 跳转到Redis解压目录下
[root@demo ~]# cd redis-5.0.5

# 编译安装
[root@demo ~]# make MALLOC=libc
[root@demo ~]# cd src && make install
```

##### 启动Redis

```shell
# 修改Redis.conf文件
[root@demo ~]# vim redis.conf
将daemonize no 修改为 daemonize yes (以后台进程方式启动Redis服务)

# 指定redis.conf文件启动
[root@demo ~]# src/redis-server ./redis.conf
```

### 1.4 MinIO

### 1.5 Java
#### 1.5.1 环境部署

```shell
# 查看Centos自带JDK是否已经安装
[root@demo ~]# yum list installed |grep java

# 若有自带安装的JDK，应进行如下操作卸载CentOS系统自带Java环境
[root@demo ~]# java -version
java version "1.7.0_181"
OpenJDK Runtime Environment (rhel-2.6.14.8.el6_9-x86_64 u181-b00)
OpenJDK Runtime Environment (rhel-2.6.14.8.el6_9-x86_64 u181-b00)

[root@demo ~]# yum -y remove java-1.7.0-openjdk*
[root@demo ~]# yum -y remove tzdata-java.noarch

# 查看yum库中的Java安装包
[root@demo ~]# yum -y list java*
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
* base: centos.ustc.edu.cn
* extras: centos.ustc.edu.cn
* updates: centos.ustc.edu.cn
base | 3.7 kB 00:00
extras | 3.4 kB 00:00
updates | 3.4 kB 00:00
Available Packages
java-1.8.0-openjdk.x86_64 1:1.8.0.101-3.b13.el6_8 updates
java-1.8.0-openjdk-debug.x86_64 1:1.8.0.101-3.b13.el6_8 updates
java-1.8.0-openjdk-demo.x86_64 1:1.8.0.101-3.b13.el6_8 updates
java-1.8.0-openjdk-demo-debug.x86_64 1:1.8.0.101-3.b13.el6_8 updates
...

# yum库中java-1.8.0为例, "*"表示将java-1.8.0的所有相关Java程序都安装上
[root@demo ~]# yum -y install java-1.8.0-openjdk*
```

### 1.6 Nginx
#### 1.6.1 环境部署
##### 安装Nginx

```shell
# Nginx的安装依赖于以下三个包，意思就是在安装Nginx之前首先必须安装一下的三个包，注意安装顺序如下

# SSL功能需要openssl库，直接通过yum安装:
[root@demo ~]# yum install openssl

# gzip模块需要zlib库，直接通过yum安装:
[root@demo ~]# yum install zlib

# rewrite模块需要pcre库，直接通过yum安装:
[root@demo ~]# yum install pcre

# 使用yum安装nginx需要包括Nginx的库，安装Nginx的库
[root@demo ~]# rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
# 使用下面命令安装nginx
[root@demo ~]# yum install nginx
```

##### 启动Nginx

```shell
[root@demo ~]# service nginx start
```

### 1.7 Maven
#### 1.7.1 环境部署

```shell
# 下载并解压
[root@demo ~]# wget http://mirrors.hust.edu.cn/apache/maven/maven-3/3.1.1/binaries/apache-maven-3.1.1-bin.tar.gz
[root@demo ~]# tar zxf apache-maven-3.1.1-bin.tar.gz
[root@demo ~]# mv apache-maven-3.1.1 /usr/local/maven3

# 配置环境变量
[root@demo ~]# vi /etc/profile
# 在文件末尾添加以下内容：
export M2_HOME=/usr/local/maven3
export PATH=$PATH:$JAVA_HOME/bin:$M2_HOME/bin

# 保存退出后使配置生效
[root@demo ~]# source /etc/profile
```

### 1.8 Git
#### 1.8.1 环境部署

```shell
# 试着输入git，看看系统有没有安装Git：
[root@demo ~]# git
The program 'git' is currently not installed. You can install it by typing:
sudo apt-get install git

# 进行安装
[root@demo ~]# sudo apt-get install git
```




## 2. 后台项目打包运行
### 2.1 从gitlab上拉取项目

```shell
[root@demo ~]# git clone -b merge3 git@gitlab.micerlabs.com:root/sparrow.git
```

### 2.2 Maven打包

```shell
[root@demo ~]# cd sparrow/backend/
[root@demo ~]# mvn package -Dmaven.test.skip=true
```

### 2.3 启动项目
项目默认端口为8089 可在启动时添加参数 --server.port=xxxx，指定端口

```
java -jar sparrow-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 3. 前端demo部署
### 3.1  解压demo压缩包

```shell
unzip dist.zip
```

### 3.2 修改Nginx配置文件

```shell
# 修改配置文件
[root@demo ~]# vim /etc/nginx/nginx.conf

# 插入Server

    server {
       listen       80; # 监听80端口
       server_name  127.0.0.1;
       gzip on; # 开启gzip压缩
       gzip_static on;
       gzip_buffers 4 16k;
       gzip_comp_level 5;
       gzip_types text/plain application/javascript text/css applciation/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;

       location / {
           root   /root/data/www/ESBP_WEB/vuepage/sparrow/dist; # demo所在文件夹路径
           index index.html;
       }
    }
    
# 保存并退出后，重启nginx
[root@demo ~]# nginx -s reload
```

## 4. 访问系统

顺利执行上述步骤后，打开浏览器，访问 http:{host}/#/doc，即可访问Sparrow系统