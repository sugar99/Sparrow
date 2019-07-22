# Sparrow 目录&权限 环境准备

> 作者：郑铠锋

[TOC]

## 1.  环境准备
### 1.1 Redis 部署
#### 1.1.1 安装Redis
```shell
# 下载Redis安装包
wget http://download.redis.io/releases/redis-5.0.5.tar.gz

# 解压压缩包
tar -zxvf redis-5.0.5.tar.gz

# 检查服务器是否有gcc依赖
gcc -v
# 若没有gcc依赖，则安装
yum insall gcc

# 跳转到Redis解压目录下
cd redis-5.0.5

# 编译安装
make MALLOC=libc
cd src && make install
```

#### 1.1.2 启动Redis

```shell
# 修改Redis.conf文件
vim redis.conf
将daemonize no 修改为 daemonize yes (以后台进程方式启动Redis服务)

# 指定redis.conf文件启动
src/redis-server ./redis.conf

# Redis服务启动成功后，进行交互
redis-cli
```

### 1.2 PostgreSQL 部署
#### 1.2.1 安装PostgresSQL

```shell
# 下载rpm包
yum install https://download.postgresql.org/pub/repos/yum/11/redhat/rhel-7-x86_64/pgdg-redhat11-11-2.noarch.rpm -y

# 安装
yum -y install postgresql11 postgresql11-server postgresql11-libs

# 初始化数据库
/usr/pgsql-11/bin/postgresql-11-setup initdb

# 设置开机自启动PostgreSQL和启动服务
systemctl enable postgresql-11
systemctl start postgresql-11
systemctl status postgresql-11

## 切换用户后进入psql
su - postgres
psql
## 修改密码
alter user postgres password '密码'
```

#### 1.2.2 允许远程连接

```shell
# 修改配置文件postgresql.conf
vi /var/lib/pgsql/11/data/postgresql.conf
#找到listen_address那里，解开注释并修改引号内localhost的值为*
listen_address="*"

# 修改配置文件pg_hba.conf
vi /var/libg/pgsql/11/data/pg_hba.conf
#在文件最后加上下面这行代码
host all all 0.0.0.0/0 trust

# 重启PostgreSQL服务
systemctl restart postgresql-11
```

## 2.  数据准备
### 2.1 PostgreSQL数据库表

| 表名           | 作用                                                       |
| -------------- | ---------------------------------------------------------- |
| spa_user       | 存放用户元数据                                             |
| spa_group      | 存放群组元数据                                             |
| spa_dir        | 存放目录元数据                                             |
| spa_doc        | 存放文档元数据                                             |
| master_slaves  | 存放目录与目录（目录与文档）之间的上下级关系，实现树形目录 |
| group_user     | 存放群组成员                                               |
| group_resource | 存放群组和资源的权限关系                                   |



### 2.2 PostgreSQL建表语句

**SPA_USER**

```sql
create table spa_user (
    user_id character varying(256) NULL,
    username character varying(256) NOT NULL,
    password character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    work_no character varying(256) NOT NULL,
    isadmin smallint NOT NULL,
    personal_dir character varying(256) NOT NULL,
    personal_group character varying(256) NOT NULL,
    PRIMARY KEY(user_id)
);
comment on column spa_user.user_id is '用户唯一标识';
comment on column spa_user.username is '用户名称';
comment on column spa_user.password is '账户密码';
comment on column spa_user.email is '邮箱地址';
comment on column spa_user.work_no is '用户工号';
comment on column spa_user.isadmin is '管理员标志位';
comment on column spa_user.personal_dir is '用户个人目录id';
comment on column spa_user.personal_group is '用户个人群组id';
```

**SPA_GROUP**

```sql
create table spa_group (
    group_id character varying(256) NOT NULL,
    group_name character varying(256) NOT NULL,
    group_desc text NOT NULL,
    creator_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    personal smallint DEFAULT 0 NOT NULL,
    PRIMARY KEY(group_id)
);
comment on column spa_group.group_id is '群组唯一标识';
comment on column spa_group.group_name is '群组名称';
comment on column spa_group.group_desc is '群组描述';
comment on column spa_group.creator_id is '群主id';
comment on column spa_group.created_at is '群组创建时间';
comment on column spa_group.personal is '个人群组标志位';
```

**SPA_DIR**

```sql
create table spa_dir(
    id character varying(256) NOT NULL,
    title character varying(256) NOT NULL,
    thumbnail character varying(256) NOT NULL,
    root smallint NOT NULL,
    home smallint NOT NULL,
    modifiable smallint NOT NULL,
    personal smallint NOT NULL,
    creator_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL,
 	PRIMARY KEY(id)
);
comment on column spa_dir.id is '目录唯一标识';
comment on column spa_dir.title is '目录名称';
comment on column spa_dir.thumbnail is '缩略图地址';
comment on column spa_dir.root is '根目录标志位';
comment on column spa_dir.home is 'home目录标志位';
comment on column spa_dir.modifiable is '可删改标志位';
comment on column spa_dir.personal is '用户个人工作区标志位';
comment on column spa_dir.creator_id is '目录创建者id';
comment on column spa_dir.created_at is '目录创建时间';
```

**SPA_DOC**

```sql
create table spa_doc(
    id character varying(256) NOT NULL,
    title character varying(256) NOT NULL,
    thumbnail character varying(256) NOT NULL,
    creator_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL,
 	PRIMARY KEY(id)
);
comment on column spa_doc.id is '文档唯一标识';
comment on column spa_doc.title is '文档名称';
comment on column spa_doc.thumbnail is '缩略图地址';
comment on column spa_doc.creator_id is '文档创建者id';
comment on column spa_doc.created_at is '文档创建时间';
```

**MASTER_SLAVES**

```sql
create table master_slaves (
    master_id character varying(256) NOT NULL,
    slave_id character varying(256) NOT NULL
);
comment on column master_slaves.master_id is '父目录id';
comment on column master_slaves.slave_id is '子目录（文档）id';
```

**GROUP_USER**

```sql
create table group_user (
    group_id character varying(256) NOT NULL,
    user_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL
);
comment on column group_user.group_id is '群组id';
comment on column group_user.user_id is '用户id';
comment on column group_user.created_at is '用户加入群组时间';
```

**GROUP_RESOURCE**

```sql
create table group_resource (
    group_id character varying(256) NOT NULL,
    resource_id character varying(256) NOT NULL,
    permission character varying(3) NOT NULL
);
comment on column group_resource.group_id is '群组id';
comment on column group_resource.resource_id is '资源id';
comment on column group_resource.permission is '操作权限';
```

