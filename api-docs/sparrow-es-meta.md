# Sparrow ES Meta

## 1.spa_docs

sparrow文档

| field         | type           | desc                                                         |
| ------------- | -------------- | ------------------------------------------------------------ |
| id            | keyword        | 文档id                                                       |
| title         | text           | 文档名称                                                     |
| desc          | text           | 文档描述                                                     |
| creator       | keyword        | 创建者id                                                     |
| files         | array[keyword] | 包含文件的id数组                                             |
| created_time  | date           | 创建时间                                                     |
| modified_time | date           | 最后修改时间                                                 |
| meta_state    | byte           | 文档是否包含元数据的标识位<br/>meta_state = 0 ：文档新创建，用户没有填写该文档相关的meta，desc等信息都是默认值 |

spa_docs mapping

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
            "created_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "modified_time": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "files": {
                "type": "keyword"
            },
            "meta_state": {
                "type": "byte"
            }
        }
    }
}
```

## 2.spa_files

sparrow文件

| field         | type           | desc                                                         |
| ------------- | -------------- | ------------------------------------------------------------ |
| id            | keyword        | 文件id                                                       |
| title         | text           | 文件名称                                                     |
| desc          | text           | 文件描述                                                     |
| creator       | keyword        | 创建者id                                                     |
| doc_id        | keyword        | 文档id                                                       |
| type          | keyword        | 文件类型 ∈ {doc, image, video, audio, others}                |
| ext           | keyword        | 文件拓展名                                                   |
| size          | long           | 文件大小                                                     |
| tags          | array[long]    | 标签id数组                                                   |
| categories    | array[long]    | 类目id数组                                                   |
| store_key     | keyword        | oss存储url                                                   |
| thumbnail     | keyword        | 缩略图url                                                    |
| derived_files | array[keyword] | 衍生文件，例如视频文件的截取帧<br/><b style="color:red">TODO：存储 id 或者 oss key？</b> |
| created_time  | date           | 创建时间                                                     |
| modified_time | date           | 最后修改时间                                                 |
| version       | byte           | 版本号                                                       |
| original_id   | keyword        | 原始文件id                                                   |
| parent_id     | keyword        | 父版本文件id                                                 |
| keywords      | array[text]    | 文档关键字（只有 type==doc 的文件有该字段）                  |
| content       | text           | 文档全文的文本信息（只有 type==doc 的文件有该字段）          |

spa_files mapping

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

## 3.spa_tags / spa_categories

文件标签 / 类目

| field | type  | desc   |
| ----- | ----- | ------ |
| id    | long  | 自增id |
| title | title | 名称   |
| desc  | title | 描述   |

spa_tags / spa_categories mapping

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







