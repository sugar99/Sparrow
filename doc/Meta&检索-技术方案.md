# Sparrow Meta&检索-技术方案

> 作者：陈绿佳、郑铠锋

[TOC]

## 1.核心技术：Elasticsearch

检索业务用到的核心技术是 [Elasticsearch](https://www.elastic.co/products/elasticsearch)，“一个分布式、RESTful 风格的搜索和数据分析引擎”。Elasticsearch相当于一个面向文档的NoSQL数据库。在Elasticsearch中，存储的最小单元被称为“文档
”（doc），数据以JSON格式存储，JSON对象每个字段内容（value）都可以被索引存储。

快速熟悉Elasticsearch（以下简称ES）的方式——阅读官方文档（**[★Elasticsearch官方文档：Elasticsearch Reference Guide](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)**），以下列出必读章节（强烈推荐）：

| 官方文档链接                                                 | 简介                                   |
| ------------------------------------------------------------ | -------------------------------------- |
| **★[Getting Started](https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html)** | 快速熟悉ES                             |
| [Document APIs](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs.html) | 文档CRUD API                           |
| [Search APIs](<https://www.elastic.co/guide/en/elasticsearch/reference/current/search.html>) | 检索API                                |
| **★[Query DSL](<https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html>)** | 检索查询语句的构造，支持丰富的查询方式 |
| [Mapping](<https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html>) | 索引字段的映射，即meta的设计           |
| **★[Aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)** | ES聚集（ES最强大突出的特性之一）       |

掌握query与aggregation的特性，结合meta的设计，可满足大多数常见的功能性检索需求。

> 建议至少读完【Getting Started】部分的文档，再继续阅读本文剩余部分。

## 2.Meta

### 2.1.Meta设计

#### A.spa_docs

sparrow文档，相当于一个包含多个sparrow文件的集合。

| field         | type           | desc                                                         |
| ------------- | -------------- | ------------------------------------------------------------ |
| id            | keyword        | 文档id（uuid），与ES的 `_id` 字段保持一致                    |
| title         | text           | 文档名称                                                     |
| desc          | text           | 文档描述                                                     |
| creator       | keyword        | 创建者id（uuid），即业务数据库中用户id                       |
| files         | array[keyword] | 包含文件的id数组，即 `spa_files` 文件对象的 `id`             |
| created_time  | date           | 创建时间                                                     |
| modified_time | date           | 最后修改时间                                                 |
| meta_state    | byte           | 文档是否包含元数据的标识位<br/>* meta_state = 0 ：文档新创建，用户没有填写该文档相关的meta，desc等信息都是默认值<br/>* meta_state = 1 ：meta数据已更新过 |

**spa_docs mapping**

```json
{
    "settings": {
        "similarity": {
            "BM25_X": {
                "type": "BM25",
                "b": 1,
                "k1": 0.2
            }
        }
    },
    "mappings": {
        "properties": {
            "id": {
                "type": "keyword"
            },
            "title": {
                "type": "text",
                "similarity": "BM25_X",
                "fields": {
                    "cn": {
                        "type": "text",
                        "similarity": "BM25_X",
                        "analyzer": "smartcn"
                    },
                    "raw": {
                        "type": "keyword"
                    }
                }
            },
            "desc": {
                "type": "text",
                "fields": {
                    "cn": {
                        "type": "text",
                        "analyzer": "smartcn"
                    }
                }
            },
            "creator": {
                "type": "keyword"
            },
            "files": {
                "type": "keyword"
            },
            "created_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "modified_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"
            },
            "meta_state": {
                "type": "byte"
            }
        }
    }
}
```

示例：

```json
{
    "id": "080d2d16-8631-4368-b562-11b7b3b4806d",
    "title": "sparrow",
    "desc": "",
    "creator": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "created_time": "2019-07-12 01:18:39.797",
    "modified_time": "2019-07-12 01:20:29.032",
    "files": [
        "a69f1f16-d92f-46b8-b97e-49c3562fbab8",
        "035f9181-3e2c-4d75-8294-d343abe31045",
        "69597d9d-1c40-40cf-97aa-7c5bda1554c3",
        "0e5f438c-2450-40e1-8698-d4faec4691c7",
        "e1e513f6-4ee6-478f-b3de-6016e15a8055",
        "c4cd7259-ad9a-4ed9-b6c9-dd73ac5be1a7"
    ],
    "meta_state": 1
}
```

#### B.spa_files

sparrow文件，相当于传统意义上的磁盘文件。

| field         | type           | desc                                                         |
| ------------- | -------------- | ------------------------------------------------------------ |
| id            | keyword        | 文档id（uuid），与ES的 `_id` 字段保持一致                    |
| title         | text           | 文件名称                                                     |
| desc          | text           | 文件描述                                                     |
| creator       | keyword        | 创建者id（uuid），即业务数据库中用户id                       |
| doc_id        | keyword        | 所属文档id，即 `spa_docs` 文档对象的 `id`                    |
| type          | keyword        | 文件类型 ∈ {doc, image, video, audio, others}                |
| ext           | keyword        | 文件拓展名                                                   |
| size          | long           | 文件大小（字节数）                                           |
| tags          | array[long]    | 标签id数组                                                   |
| categories    | array[long]    | 类目id数组                                                   |
| store_key     | keyword        | oss存储url                                                   |
| thumbnail     | keyword        | 缩略图url                                                    |
| derived_files | array[keyword] | 衍生文件，例如视频文件的截取帧<br/><b style="color:red">TODO：存储 id 或者 oss key？</b> |
| created_time  | date           | 创建时间                                                     |
| modified_time | date           | 最后修改时间                                                 |
| version       | byte           | 版本号，默认为0                                              |
| original_id   | keyword        | 原始文件id，如果是初始版本，则与id相同                       |
| parent_id     | keyword        | 父版本文件id                                                 |
| keywords      | array[text]    | 文档关键字（只有 `type=doc` 的文件有该字段）                 |
| content       | text           | 文档全文的文本信息（只有 `type=doc` 的文件有该字段）         |

**spa_files mapping**

```json
{
    "settings": {
        "similarity": {
            "BM25_X": {
                "type": "BM25",
                "b": 1,
                "k1": 0.2
            }
        }
    },
    "mappings": {
        "properties": {
            "id": {
                "type": "keyword"
            },
            "title": {
                "type": "text",
                "similarity": "BM25_X",
                "fields": {
                    "cn": {
                        "type": "text",
                        "similarity": "BM25_X",
                        "analyzer": "smartcn"
                    },
                    "raw": {
                        "type": "keyword"
                    }
                }
            },
            "desc": {
                "type": "text",
                "fields": {
                    "cn": {
                        "type": "text",
                        "analyzer": "smartcn"
                    }
                }
            },
            "creator": {
                "type": "keyword"
            },
            "doc_id": {
            	"type": "keyword"
            },
            "type": {
                "type": "keyword"
            },
            "ext": {
                "type": "keyword"
            },
            "tags": {
                "type": "long"
            },
            "categories": {
                "type": "long"
            },
            "store_key": {
                "type": "keyword",
                "index": false
            },
            "thumbnail": {
                "type": "keyword",
                "index": false
            },
            "derived_files": {
                "type": "keyword",
                "index": false
            },
            "created_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "modified_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "version": {
                "type": "byte"
            },
            "original_id": {
                "type": "keyword",
                "index": false
            },
            "parent_id": {
                "type": "keyword",
                "index": false
            },
            "keywords": {
                "type": "text",
                "similarity": "BM25_X",
                "fields": {
                    "cn": {
                        "type": "text",
                        "similarity": "BM25_X",
                        "analyzer": "smartcn"
                    },
                    "raw": {
                        "type": "keyword"
                    }
                }
            },
            "content": {
                "type": "text"
                
            }
        }
    }
}
```

示例：

```json
{
    "id": "035f9181-3e2c-4d75-8294-d343abe31045",
    "title": "sparrow检索需求",
    "desc": "sparrow文件的检索需求（不包含文档）",
    "creator": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "type": "doc",
    "ext": "pdf",
    "size": 818623,
    "doc_id": "080d2d16-8631-4368-b562-11b7b3b4806d",
    "tags": [1, 50, 1024],
    "categories": [0, 2, 6],
    "created_time": "2019-07-12 02:17:24",
    "modified_time": "2019-07-12 02:17:24",
    "store_key": "doc/035f9181-3e2c-4d75-8294-d343abe31045.pdf",
    "thumbnail": "http://39.108.210.48:9000/douban-test/image/thumbnail/7573cdae-5a68-4d44-9c97-dc98fbe1c490.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=SWLNR4NMXK02HG0K6BM6%2F20190711%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190711T181740Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=30c2440e79bb87f3760e123715f3e37009eea08c8fcb5065ba5a3be407edf12e",
    "derived_files": [],
    "version": 0,
    "original_id": "035f9181-3e2c-4d75-8294-d343abe31045",
    "parent_id": null,
    "keywords": ["检索", "文件", "需求"],
    "content": "......"
}
```

#### C.spa_tags / spa_categories

文件标签 / 类目

| field | type  | desc                                            |
| ----- | ----- | ----------------------------------------------- |
| id    | long  | 自增id，同时设置索引的 `_id` 字符串和 `id` 相同 |
| title | title | 名称                                            |
| desc  | title | 描述                                            |

**spa_tags / spa_categories mapping**

```json
{
    "settings": {
        "similarity": {
            "BM25_X": {
                "type": "BM25",
                "b": 1,
                "k1": 0.2
            }
        }
    },
    "mappings": {
        "properties": {
            "id": {
                "type": "long"
            },
            "title": {
                "type": "text",
                "similarity": "BM25_X",
                "fields": {
                    "cn": {
                        "type": "text",
                        "similarity": "BM25_X",
                        "analyzer": "smartcn"
                    },
                    "raw": {
                        "type": "keyword"
                    }
                }
            },
            "desc": {
                "type": "text",
                "fields": {
                    "cn": {
                        "type": "text",
                        "analyzer": "smartcn"
                    }
                }
            }
        }
    }
}
```

示例：

```json
{
    "id": 95,
    "title": "Java",
	"desc": "世界上最好的编程语言"
}
```

#### D.spa_groups

用户与群组，和业务数据库中 `user_t` , `group_t` 数据保持一致。便于用户群组的搜索。



### 2.2.Meta维护









## 3.检索实现

### 3.1.文件文档检索

#### A.搜索建议 [S1接口]

<img src="../assets/search-suggestion.png">

| 接口作用 | 获取搜索建议 |
| -------- | ------------ |
| 输入     |              |
| 输出     |              |
| 原理     |              |
|          |              |



#### B.获取相关 [S2接口]



#### C.搜索结果 [S3接口]



### 3.2.标签类目检索 [S4接口]



### 3.3.用户群组检索 [S5接口]







### 3.4.待完善需求

#### 3.4.1.关键词检索 & 全文检索



#### 3.4.2.搜索结果高亮



#### 3.4.3.模糊搜索

建议只匹配长度短的字段（例如 `title`）



