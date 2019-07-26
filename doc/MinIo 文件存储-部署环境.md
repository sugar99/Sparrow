#### Minio部署

------

sparrow项目里Minio部署到阿里云服务器（39.108.210.48），这里演示的是Linux下的部署，其他详细信息可以查看[官方文档](https://docs.min.io/docs/minio-quickstart-guide.html)。

##### 下载

```
wget https://dl.min.io/server/minio/release/linux-ppc64le/minio
```

##### 启动

```
./minio server /data
```

/data是指定数据存放的位置，路径不一样，accessKey和secretKey会不一样。sparrow项目里数据存放的路径位置为/root/minio/data，accessKey和secretKey分别如下。

accessKey : SWLNR4NMXK02HG0K6BM6

secretKey : 1sPE5q7oNEHpWPBZWdOgttFZyOg+YroItnq9P7wn

##### 查看

可以在浏览器访问链接查看。

```
http://39.108.210.48:9000
```

