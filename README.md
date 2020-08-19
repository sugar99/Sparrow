2019-10-25更新：服务器的postgres数据库被黑了，里面全部数据都被锁定，以致成果页面建的测试/展示目录都没了。预计11月中旬结课后进行修复。

![2019_09_27_10_56_IMG_2657](assets/2019_09_27_10_56_IMG_2657.PNG)

# Sparrow

- [1.项目结构](#1项目结构)
  - [1.1.项目目录](#11项目目录)
  - [1.2.前端代码结构](#12前端代码结构)
  - [1.3.后端代码结构](#13后端代码结构)

## 1.项目结构

### 1.1.项目目录

```lua
Sparrow
├── README.md
├── api -- API文档
├── doc -- 文档制品
├── data -- 数据
├── assets -- 图片资源
├── frontend -- 前端
├── backend -- 后端
├── backend-legacy -- 宇伦师兄的后端代码

```

### 1.2.前端代码结构


### 1.3.后端代码结构

```lua
src/main/java/com/micerlab/sparrow
├── controller -- 控制器（API）
├── service -- 服务层（业务逻辑）
├── dao -- 数据读写接口
├── mapper --  数据库映射文件
├── config -- SpringBoot配置类等
├── domain -- 领域层（业务逻辑所需的类等）
├── util -- 封装的工具类
```






