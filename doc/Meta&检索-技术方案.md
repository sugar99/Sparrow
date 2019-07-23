# Sparrow Meta&检索-技术方案

> 作者：陈绿佳、郑铠锋

[TOC]

## 1.核心技术：Elasticsearch

检索业务用到的核心技术是 [Elasticsearch](https://www.elastic.co/products/elasticsearch)，“一个分布式、RESTful 风格的搜索和数据分析引擎”。Elasticsearch相当于一个面向文档的NoSQL数据库。在Elasticsearch中，存储的最小单元被称为“文档
”（doc），数据以JSON格式存储，JSON对象每个字段内容（value）都可以被索引存储。

### 1.1.ES 官方文档

快速熟悉Elasticsearch（以下简称ES）的方式——阅读官方文档（**[★Elasticsearch官方文档：Elasticsearch Reference Guide](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)**），以下列出**必读章节（强烈推荐）**：

| 官方文档链接                                                 | 简介                                   |
| ------------------------------------------------------------ | -------------------------------------- |
| ★[Elastic Stack and Product Documentation](https://www.elastic.co/guide/index.html) | Elastic技术栈文档目录                  |
| **★[Getting Started](https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html)** | 快速熟悉ES                             |
| [Document APIs](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs.html) | 文档对象CRUD API                       |
| [Search APIs](<https://www.elastic.co/guide/en/elasticsearch/reference/current/search.html>) | 检索API                                |
| **★[Query DSL](<https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html>)** | 检索查询语句的构造，支持丰富的查询方式 |
| [Mapping](<https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html>) | 索引字段的映射，即meta的设计           |
| **★[Aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)** | ES聚集（ES最强大突出的特性之一）       |
| [Java Rest High Level Client](<https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html>) | Java API文档，ES查询语句 ⇨ Java代码    |
| [Analysis](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html) | 分词、索引                             |

掌握query与aggregation的特性，结合meta的设计，可满足大多数常见的功能性检索需求。

> 建议至少读完【Getting Started】部分的文档，再继续阅读本文剩余部分。

### 1.2.数据迁移工具

| 链接                                                         | 简介                             |
| ------------------------------------------------------------ | -------------------------------- |
| ★[Getting Started with Logstash](https://www.elastic.co/guide/en/logstash/current/getting-started-with-logstash.html) | Logstash：ES数据导入、迁移工具   |
| ★[elasticdump](https://github.com/taskrabbit/elasticsearch-dump) | ES数据（轻量）导入导出工具       |

### 1.3.检索原理与优化

| 链接                                                         | 简介                             |
| ------------------------------------------------------------ | -------------------------------- |
| [如何使用 Elasticsearch 6.2 搜索中文、日文和韩文文本](https://www.elastic.co/cn/blog/how-to-search-ch-jp-kr-part-1) | 分词器与多字段方法，增强搜索效果 |
| [Multi-fields](https://www.elastic.co/guide/en/elasticsearch/reference/current/multi-fields.html) | 多字段                           |
| [Indexing for Beginners](https://www.elastic.co/cn/blog/found-indexing-for-beginners-part3) | 存储与检索：分词、倒排索引       |
| [Similarity Module](https://www.elastic.co/guide/en/elasticsearch/reference/current/index-modules-similarity.html) | 匹配评分算法                     |
| [Highlighting](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-highlighting.html#offsets-strategy) | 检索结果高亮                     |

### 1.4.检索UI Demo

| 链接                                                         | 简介                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [SearchKit](https://github.com/searchkit/searchkit)<br/>[SearchKit Demo](http://demo.searchkit.co/) | 开源ES前端UI<br/>✓ 侧边栏<br/>✓ 图标模式 \| 列表模式 <br/>✓ 前端控件构造ES查询语句 |
| [51job职位搜索](https://search.51job.com/)                   | ✓ 过滤条件布局                                               |
| [淘宝](https://www.taobao.com/)                              | ✓ 搜索建议联动<br/>✓ 标签类目、过滤选项的布局                |
| [当当 高级搜索](http://search.dangdang.com/advsearch?is_batch=1) | ✓ 多字段搜索                                                 |

### 1.5.其它链接

| 链接                                                         | 简介                                                |
| ------------------------------------------------------------ | --------------------------------------------------- |
| [Awesome Elasticsearch](https://github.com/dzharii/awesome-elasticsearch) | ES资源汇总（文档、工具、开源项目）                  |
| [elasticsearch-definitive-guide](https://github.com/elastic/elasticsearch-definitive-guide) | ES较权威的书（停留在ES 2.0版本，目前ES已更新至7.2） |
| [Elasticsearch史上最全最常用工具清单](https://blog.csdn.net/laoyang360/article/details/80645710) | ES集群、运维工具                                    |

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
                "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
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
            "size": {
                "type": "long"
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
                "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "modified_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
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

| field | type | desc                                                        |
| ----- | ---- | ----------------------------------------------------------- |
| id    | long | 自增id（整数），同时设置索引的 `_id` （字符串）和 `id` 相同 |
| title | text | 名称                                                        |
| desc  | text | 描述                                                        |

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

#### D.spa_users / spa_groups

用户与群组，和业务数据库中 `user_t` , `group_t` 数据保持一致。便于用户群组的搜索。

| field | type | desc                           |
| ----- | ---- | ------------------------------ |
| id    | text | uuid，与业务数据库中id保持一致 |
| name  | text | 名称                           |
| desc  | text | 描述（该字段暂时没用上）       |

**spa_users / spa_groups mapping**

```json
{
    "settings": {
        "index": {
            "number_of_shards": 5,
            "number_of_replicas": 2
        }
    },
    "mappings": {
        "mapping": {
            "_doc": {
                "properties": {
                    "name": {
                        "type": "text",
                        "analyzer": "smartcn"
                    }
                }
            }
        }
    }
}
```

### 2.2.Meta维护

Meta数据的维护基本是ES数据的CRUD操作，有些操作对外提供API服务，而有些操作只在service层和dao层提供方法给其它业务调用。

| 接口                     | 实现                                     |
| ------------------------ | ---------------------------------------- |
| D7.获取文档Meta          |                                          |
| D8.更新文档Meta          | 只允许部分字段更新                       |
| F5. 创建文件Meta         |                                          |
| F9.获取文件Meta          |                                          |
| F10.更新文件Meta         | 只允许部分字段更新                       |
| F11-F14 标签类目CRUD     |                                          |
| F15.获取文件的类目或标签 |                                          |
| F16.更新文件的类目或标签 | 该接口同时用于类目标签的新增、更新、删除 |

#### 2.2.1.创建文件Meta [F5接口]

| 参数      | 含义                                                         |
| --------- | ------------------------------------------------------------ |
| title     | 文件名                                                       |
| store_key | 文件在oss中的key                                             |
| doc_id    | 当前上传文件所属的文档ID                                     |
| parent_id | 父版本文件id<br/>当前上传文件如果为某一文件的新版本，则需要传其父版本文件的ID，否则为null |
| ext       | 文件后缀名                                                   |
| size      | 文件的大小（字节数）                                         |
| creator   | 创建者id（从后端鉴权服务获取）                               |

客户端上传文件至oss存储后，调用该接口存储文件Meta，业务流程如下（不包括鉴权）：

1. 获取指定doc_id的文档Meta
2. 检测文档files数组是否包含parent_id
3. 用请求参数构造新文件Meta
4. 如果parent_id不为null，获取指定parent_id的文件Meta，用父版本文件Meta的部分字段填充新文件的Meta
5. 若文档的files数组包含给定parent_id，用file_id替换parent_id，否则在files数组中插入file_id。更新文档modified_time字段
6. 将新文件Meta，文档Meta分别写入ES

7. 使用异步消息处理文件缩略图，提取（文档类型的）文件全文字段、关键词，将这些字段写入ES

## 3.检索实现

### 3.1.文件文档检索

#### A.搜索建议 [S1接口]

<img src="../assets/search-type.png">

| 接口作用 | 获取搜索建议                                                 |
| -------- | ------------------------------------------------------------ |
| 输入     | * 搜索类型 type<br/>* 关键词 keyword                         |
| 输出     | 在选定搜索类型限制下，与关键词相匹配的【文件标题】、【类目】、【标签】（去除重复） |
| 原理     | 1. 同时搜索spa_files, spa_tags, spa_categories 3个索引，匹配title字段<br/>2. 对spa_files过滤type字段，限制文件类型<br/>3. 聚集title字段（去除重复），按step1中匹配的分数排序 |

SQL伪代码为：

> 设匹配函数 `boolean match(field, keyword)` ，即用关键词匹配特定字段，并在匹配的记录中插入 `match_score` 作为匹配的分数。

```mysql
SELECT `title`
FROM (SELECT * FROM spa_files WHERE match(`title`, ${keyword}) AND `type` = ${type})
	UNION (SELECT * FROM spa_tags WHERE match(`title`, ${keyword}))
	UNION (SELECT * FROM spa_categories WHERE match(`title`, ${keyword}))
GROUP BY `title`
ORDER BY MAX(`match_score`) DESC 
LIMIT 10;
```

Query DSL

```http
GET {{es-remote-url}}/spa_files,spa_tags,spa_categories/_search?size=0
```

Request Body

```json
{
    "query": {
        "bool": {
            "must": {
                "multi_match": {
                    "query": "算法",
                    "fields": [
                        "title",
                        "title.cn^3"
                    ]
                }
            },
            "filter": {
                "script": {
                    "script": {
                        "source": "!doc.containsKey('type') || doc.containsKey('type') && doc['type'].value.equals(params.type)",
                        "params": {
                            "type": "image"
                        }
                    }
                }
            }
        }
    },
    "aggs": {
        "group_by_title": {
            "terms": {
                "field": "title.raw",
                "order": {
                    "term_score.value": "desc"
                }
            },
            "aggs": {
                "term_score": {
                    "max": {
                        "script": {
                            "source": "_score"
                        }
                    }
                }
            }
        }
    }
}
```

#### B.获取相关 [S2接口]

<img src="../assets/search-suggestion.png">

| 接口作用 | 获取高度相关的类目标签                                       |
| -------- | ------------------------------------------------------------ |
| 输入     | 关键词 keyword                                               |
| 输出     | 对于与关键词相匹配的文件，这些文件中包含次数最多的n个类目标签 |
| 原理     | 1. 搜索spa_files索引，匹配title字段<br/>2. 分别聚集tags字段和categories字段，得到词频最高的标签id和类目id<br/>3. 分别从spa_tags和spa_categories中获取这些id的记录 |

假设文件与标签的外联表为 `file_tag_t` (`file_id`, `tag_id`)，获取标签的伪SQL查询语句：

```sql
WITH F as (
    SELECT `id`
    FROM `spa_files`
    WHERE match(`title`, ${keyword})
), T as (
	SELECT `tag_id`
	FROM `file_tag_t` as FT join F on FT.file_id = F.id
	GROUP BY FT.`tag_id`
    ORDER BY COUNT(F.id)
)
SELECT *
FROM `spa_tags` as S join T on S.id = T.tag_id
LIMIT 5;
```

> 注：伪SQL语句只为解释ES查询语句的原理，实际上并不存在！

Query DSL

```http
GET {{es-remote-url}}/spa_files/_search?size=0
```

Request Body

```json
{
    "query": {
        "multi_match": {
            "query": "算法导论",
            "fields": [
                "title",
                "title.cn^3"
            ]
        }
    },
    "aggs": {
    	"top_tags": {
    		"terms": {
    			"field": "tags",
    			"size": 5
    		}
    	},
    	"top_categories": {
    		"terms": {
    			"field": "categories",
    			"size": 5
    		}
    	}
    }
}
```

#### C.搜索结果 [S3接口]

<img src="../assets/search-result.png">

| 接口作用 | 获取文件搜索结果                                             |
| -------- | ------------------------------------------------------------ |
| 输入     | * 搜索类型 type<br/>* 关键词 keyword<br/>* 选中标签id数组 tags<br/>* 选中类目id数组 categories<br/>* 选中拓展名数组 exts<br/>* 创建时间选取区间 created_time<br/>* 修改时间选取区间 modified_time |
| 输出     | 1. 匹配以上关键词与所有过滤条件的文件记录<br/>2. 在限制了创建时间区间、修改时间区间的条件下，各个拓展名所包含的文件数量<br/>3. 在限制了拓展名的条件下，各个创建时间、各个修改时间所包含的文件数量 |
| 原理     | 1. 搜索spa_files索引，得到文件集合D1<br/>    A.限定type字段<br/>    B.用keyword匹配title和desc字段<br/>    C.限定tags字段匹配tags数组中的所有标签<br/>    D.限定categories字段匹配categories数组中的所有类目<br/>2. 由D1过滤ext字段，得到D2<br/>3. 从D2，分别聚集created_time字段和modified_time字段，得到各个时间维度所包含的文件数量<br/>4. 对D1中的文件限定created_time和modified_time字段，得到D3<br/>5. 从D3，聚集ext字段，得到各个拓展名维度所包含的文件数量<br/>6. 自D3限制ext字段，得到最终匹配搜索的文件结果D4<br/>7. 高亮搜索结果 |

用【管道过滤器】模式描述上述流程，其中包含filter的是过滤器管道，包含agg的是聚集管道。上一级管道的输出可作为下一级管道的输入。

![1563799735233](assets/1563799735233.png)

 详细流程如下：

![1563847877508](assets/1563847877508.png)

其实直接使用所有过滤条件，从 D0 > D1 > D4 的路径，也是可以得到最终的搜索结果的。不过由于D3处的结果已经使用了大多数过滤条件，继续再做个拓展名过滤，便可得到最终的搜索结果，利用了之前两个过滤管道累积的计算结果，计算开销更小。最终采用的方式是 D0 > D1 > D3 > D4 。

Query DSL

```http
GET {{es-remote-url}}/spa_files/_search?size=0
```

Request Body

```json
{
     "query": {
        "bool": {
            "must": {
                "multi_match": {
                    "query": "算法",
                    "fields": [
                        "title",
                        "title.cn^3",
                        "desc",
                        "desc.cn"
                    ]
                }
            },
            "filter": [
                {
                    "term": {
                        "type": "image"
                    }
                },
                {
                    "terms_set": {
                        "tags": {
                            "terms": [
                                133,
                                137
                            ],
                            "minimum_should_match_script": {
                                "source": "2"
                            }
                        }
                    }
                },
                {
                    "terms_set": {
                        "categories": {
                            "terms": [
                                0,
                                6
                            ],
                            "minimum_should_match_script": {
                                "source": "2"
                            }
                        }
                    }
                }
            ]
        }
    },
    "aggs": {
        "exts_limit": {
            "filter": {
                "terms": {
                    "ext": [
                        "jpg",
                        "gif"
                    ]
                }
            },
            "aggs": {
                "created_time_ranges": {
                    "date_range": {
                        "field": "created_time",
                        "format": "yyyy-MM-dd",
                        "time_zone": "+8",
                        "ranges": [
                            {
                                "key": "三天内",
                                "from": "now-3d/d"
                            },
                            {
                                "key": "一周内",
                                "from": "now-1w/d"
                            },
                            {
                                "key": "一个月内",
                                "from": "now-1M/d"
                            },
                            {
                                "key": "三个月内",
                                "from": "now-3M/d"
                            },
                            {
                                "key": "半年内",
                                "from": "now-6M/d"
                            },
                            {
                                "key": "一年内",
                                "from": "now-1y/d"
                            },
                            {
                                "key": "全部",
                                "to": "now"
                            }
                        ]
                    }
                },
                "modified_time_ranges": {
                    "date_range": {
                        "field": "modified_time",
                        "format": "yyyy-MM-dd",
                        "time_zone": "+8",
                        "ranges": [
                            {
                                "key": "三天内",
                                "from": "now-3d/d"
                            },
                            {
                                "key": "一周内",
                                "from": "now-1w/d"
                            },
                            {
                                "key": "一个月内",
                                "from": "now-1M/d"
                            },
                            {
                                "key": "三个月内",
                                "from": "now-3M/d"
                            },
                            {
                                "key": "半年内",
                                "from": "now-6M/d"
                            },
                            {
                                "key": "一年内",
                                "from": "now-1y/d"
                            },
                            {
                                "key": "全部",
                                "to": "now"
                            }
                        ]
                    }
                }
            }
        },
        "time_ranges_limit": {
            "filter": {
                "bool": {
                    "filter": [
                        {
                            "range": {
                                "created_time": {
                                    "time_zone": "+8",
                                    "lte": "now"
                                }
                            }
                        },
                        {
                            "range": {
                                "modified_time": {
                                    "time_zone": "+8",
                                    "lte": "now"
                                }
                            }
                        }
                    ]
                }
            },
            "aggs": {
                "group_by_ext": {
                    "terms": {
                        "field": "ext"
                    }
                },
                "final_exts_limit": {
                    "filter": {
                        "terms": {
                            "ext": [
                                "jpg",
                                "gif"
                            ]
                        }
                    },
                    "aggs": {
                        "results": {
                            "top_hits": {
                                "from": 0,
                                "size": 10,
                                "highlight": {
                                    "fields": {
                                        "title": {
                                            "number_of_fragments": 0,
                                            "no_match_size": 50
                                        },
                                        "desc": {},
                                        "keywords": {},
                                        "content": {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
```

### 3.2.标签类目检索 [S4接口]

简单的检索，用关键词匹配标签（类目）名称和详情字段即可。

| 接口作用 | 获取高度相关的类目标签                             |
| -------- | -------------------------------------------------- |
| 输入     | 关键词 keyword                                     |
| 输出     | 与关键词相匹配的标签（类目）                       |
| 原理     | 搜索spa_tags索引，用keyword匹配title字段和desc字段 |

### 3.3.用户群组检索 [S5,S6接口]

原理基本与[3.2.标签类目检索]相同。

### 3.4.待完善需求

* [ ] 模糊搜索

* [ ] 文件版本

#### 3.4.1.文件版本

文件版本管理。



#### 3.4.2.模糊搜索

建议只匹配长度短的字段（例如 `title`）





## 4.代码实现







