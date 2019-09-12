# Sprrow业务数据库-数据模型设计

## 1. SPA_USER

| Field          | Type         | Nullable | Default | Description                |
| -------------- | ------------ | -------- | ------- | -------------------------- |
| user_id        | varchar(64)  | Not Null | \       | 用户唯一标识(Java生成uuid) |
| username       | varchar(256) | Not Null | \       | 用户名称                   |
| password       | varchar(256) | Not Null | \       | 用户登录密码               |
| email          | varchar(256) | Not Null | \       | 邮箱地址                   |
| work_no        | varchar(256) | Not Null | \       | 用户工号                   |
| personal_dir   | varchar(64)  | Not Null | \       | 用户个人目录（工作区）id   |
| personal_group | varchar(64)  | Not Null | \       | 用户个人群组id             |
| isadmin        | smallint     | Null     | 0       | 管理员标志位               |

* isadmin为1时表示该员工为管理员，具有管理员权限；
* personal_group，创建用户同时创建用户个人群组，起授权作用。

## 2. SPA_GROUP

| Field      | Type         | Nullable | Default | Description  |
| ---------- | ------------ | -------- | ------- | ------------ |
| group_id   | varchar(64)  | Not Null | \       | 群组唯一标识 |
| group_name | varchar(256) | Not Null | \       | 群组名称     |
| group_desc | text         | Not Null | \       | 群组描述     |
| creator_id | varchar(64)  | Not Null | \       | 群主id       |
| created_at | timestamp    | Not Null | \       | 群组创建时间 |
| personal   | smallint     | Null     | 0       | 个人群组标志 |

* personal为1时表示该群组为用户个人群组

## 3. SPA_DIR

| Field      | Type         | Nullable | Default | Description    |
| ---------- | ------------ | -------- | ------- | -------------- |
| id         | varchar(64)  | Not Null | \       | 目录唯一标识   |
| title      | varchar(256) | Not Null | \       | 目录名称       |
| thumbnail  | varchar(256) | Not Null | \       | 缩略图地址     |
| creator_id | varchar(64)  | Not Null | \       | 创建者id       |
| created_at | timestamp    | Not Null | \       | 创建时间       |
| root       | smallint     | Null     | 0       | 根目录标志位   |
| home       | smallint     | Null     | 0       | home目录标志位 |
| personal   | smallint     | Null     | 0       | 个人目录标志位 |
| modifiable | smallint     | Null     | 1       | 可修改标志位   |

* root为1时，表示该目录为根目录
* home为1时，表示该目录为home目录（home目录下挂载用户个人工作区）
* personal为1时，表示该目录为个人目录（个人工作区）
* modifiable为0时，表示该目录不可修改

## 4. SPA_DOC

| Field      | Type         | Nullable | Default | Description  |
| ---------- | ------------ | -------- | ------- | ------------ |
| id         | varchar(64)  | Not Null | \       | 文档唯一标识 |
| title      | varchar(256) | Not Null | \       | 文档名称     |
| thumbnail  | varchar(256) | Not Null | \       | 缩略图地址   |
| creator_id | varchar(64)  | Not Null | \       | 创建者id     |
| created_at | timestamp    | Not Null | \       | 创建时间     |

## 5. MASTER_SLAVES

| Field     | Type        | Nullable | Default | Description |
| --------- | ----------- | -------- | ------- | ----------- |
| master_id | varchar(64) | Not Null | \       | 父资源id    |
| slave_id  | varchar(64) | Not Null | \       | 子资源id    |

通过该表形成树形目录结构

## 6. GROUP_USER

| Field      | Type        | Nullable | Default | Description        |
| ---------- | ----------- | -------- | ------- | ------------------ |
| group_id   | varchar(64) | Not Null | \       | 群组id             |
| user_id    | varchar(64) | Not Null | \       | 用户id             |
| created_at | timestamp   | Not Null | \       | 用户加入群组的时间 |

## 7. GROUP_RESOURCE

| Field       | Type        | Nullable | Default | Description        |
| ----------- | ----------- | -------- | ------- | ------------------ |
| group_id    | varchar(64) | Not Null | \       | 群组id             |
| resource_id | varchar(64) | Not Null | \       | 资源id(目录和文档) |
| permission  | varchar(3)  | Not Null | \       | 操作权限           |

* permission: "100"表示可读；"010"表示可写；"001"表示可执行，“110"表示可读可写……

