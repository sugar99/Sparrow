# Spa测试数据准备

| 文件                | 描述            |
| ------------------- | --------------- |
| data.csv            | 原始爬虫数据    |
| format/images.csv   | 图片id与oss_key |
| format/docs.csv     | 文档id与oss_key |
| format/keywords.csv | 文档id与关键词  |

> format目录下返回数据格式，**实际生成数据请放置于data目录下**

## 0.豆瓣图书数据

原始爬虫数据为 `data.csv` ，每一行是一条图书数据，`id` 是图书的唯一编号， `pic_url` 是图片链接，其余字段为与图书相关的结构化文本信息。

通过 `pic_url` 可获取到**压缩过的图片**，<b style="color:red">获取原图需要在链接后面加上 `p=0` 的参数</b>。`pic.py` 中给出了下载图片的示例代码。

```http
GET http://116.56.140.131:4869/89473df3082687d8bf4f2e2543810534?p=0
```


![](http://116.56.140.131:4869/89473df3082687d8bf4f2e2543810534?p=0)

## 1.生成图片数据

利用 `id` 和 `pic_url` 字段，获取存储在zimg的图片，将图片写入oss，将返回的 `oss_key` 与 `id` 记录在一起。数据文件格式：`format/images.csv` 

<b style="color:red">【注意】zimg服务器部署在华工内网，需要用校园网访问。</b>

## 2.生成文档数据

利用  `id` 和其它文本字段，将每条记录生成一个 `docx` 或 `pdf` 文件，存入oss，将返回的 `oss_key` 与 `id` 记录在一起。数据文件格式：`format/docs.csv` 

## *3.提取文档关键词

1. 提取文档的文本信息
2. 使用开源工具提取文本信息的关键词（3~5个）

数据文件格式：`format/keywords.csv` 

【文本信息关键词的提取】可以考虑使用TF-IDF方法或其它开源工具。