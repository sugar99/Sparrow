# upload

## 主要用到的东西
 - vue用于前端框架
 - [element ui](https://element.eleme.cn/#/zh-CN/component/installation)和[ant-design-vue](https://vue.ant.design/docs/vue/introduce-cn/)用于前端样式
 - [vue cli3](https://cli.vuejs.org/zh/)
 - [vue router](https://router.vuejs.org/zh/installation.html)
 - [vuex](https://vuex.vuejs.org/)



## 文件结构

![1563720071820](./public/1563720071820.png)



## 待解决

- 未完成组件：群组鉴权、历史版本

- 看老师意思：个人信息和部门文档

- bug：

  - [x] 文档、文件删除无法取消。
  - [ ] 从检索回来时面包屑路径没改。
  - [ ] 预览。
  - [ ] 删除群组 标号退出后还存在的。
  - [ ] 新建文件或文件夹 排序不正确 名字无法修改。
  - [ ] 交互提示过于单一，不够明确。
  - [ ] 文件被锁定问题。
  - [ ] 提示网络有问题后，那个文件夹好像被锁定了，不能选别的文件夹，只能刷新页面。
  - [ ] 一次只能打开一个文件预览。
  - [ ] 交互提示被阴影遮盖
  - [ ] 新建用户接口403
  - [ ] 新建 不刷新问题
  - [ ] 8090端口上传失败

- 不够优雅的地方：

  - [ ] 点击时的过渡动画

  - [ ] 点属性时的过渡动画
  
  - [ ] 文件文档目录图标格式单一
  
    

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
