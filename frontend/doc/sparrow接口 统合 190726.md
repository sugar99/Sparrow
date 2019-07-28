### 用户模块

#### U1.新建用户

[POST] /v1/users

Request Body

```json
{
    "work_no" : "7788", // 员工工号
    "username" : "hello", // 用户名
    "password" : "mypassword", // 密码
    "email" : "hello@gmail.com" // 邮箱
}
```

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}
```

#### U2.用户登录

[POST] /v1/login

Request Body

```json
{
    "work_no": "7856", // 员工工号
    "password": "mypassword" // 用户密码
}
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "userInfo": {
      "work_no": "0001",
      "email": "zhangSan@gmail.com",
      "username": "张三"
    },
    "master_dirs": [
      {
        "resource_id": "b64b725b-ef13-4e3f-9d98-ddb3152981a6",
        "resource_name": "home"
      },
      {
        "resource_id": "e3a10377-edcc-4a8a-8cce-396b0e223f48",
        "resource_name": "root"
      }
    ],
    "resource_id": "b57a392d-6510-4116-99db-f76cc16a78e5",
    "resource_name": "张三"
  }
}
```

#### U3.获取用户元数据

[GET] /v1/users/{user_id}

- user_id: 用户id，可替换为current（查询当前用户的元数据）

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "userInfo": {
      "work_no": "0001",
      "email": "zhangSan@gmail.com",
      "username": "张三"
    },
    "master_dirs": [
      {
        "resource_id": "b64b725b-ef13-4e3f-9d98-ddb3152981a6",
        "resource_name": "home"
      },
      {
        "resource_id": "e3a10377-edcc-4a8a-8cce-396b0e223f48",
        "resource_name": "root"
      }
    ],
    "resource_id": "b57a392d-6510-4116-99db-f76cc16a78e5",
    "resource_name": "张三"
  }
}
```

#### U4.注销登录

[POST] /v1/users/logout

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}
```

#### U5.获取用户所在群组

[GET] /v1/users/{user_id}/groups

- user_id: 用户id，可替换为current

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": [
    {
      "group_id": "16bd8e6b-d05d-4dfc-b6ce-ebfca1efc228",
      "group_name": "admin",
      "group_desc": "管理员",
      "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
      "created_at": "2019-07-08T09:54:21.121+0000",
      "personal": 0
    }
  ]
}
```

### 群组模块

#### G1.新建群组

[POST] /v1/groups/

Request Body

```json
{
    "group_name": "programmer", // 群组名称
    "group_desc": "苦逼程序员" // 群组描述字段
}
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "group_id": "92d8e6c3-a661-4157-814c-a3838ef83456",
    "group_name": "巴萨球迷",
    "group_desc": "巴萨是最菜的",
    "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "created_at": "2019-07-09T16:58:05.462+0000",
    "personal": 0
  }
}

```

#### G2.获取群组元数据

[GET] /v1/groups/{group_id}

- group_id: 群组id

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "group_id": "92d8e6c3-a661-4157-814c-a3838ef83456",
    "group_name": "巴萨球迷",
    "group_desc": "巴萨是最菜的",
    "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "created_at": "2019-07-09T16:58:05.462+0000",
    "personal": 0
  }
}

```

#### G3.修改群组元数据

[PUT] /v1/groups/{group_id}

- group_id: 群组id

Request Body

```json
{
    "group_name": "newbility", // 群组名称
    "group_desc": "非常牛b" // 群组描述字段
}

```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": null
}

```

#### G4.删除群组

[DELETE] /v1/groups/{group_id}

- group_id: 群组id

Resposne Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```

#### G5.添加群组用户

[POST] /v1/groups/{group_id}/members

- group_id: 群组id

Request Body

```json
{
	// 用户id列表
    "usersIdList": [
        "1cbbf901-24ad-40fd-a35a-5dce15c82333",
        "24c16cd4-a2e6-4bd5-91b2-ab51c87b7514",
        "6272b618-a4e5-49c3-a4cb-69a94605c692"
    ]
}

```

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```

#### G6.获取群组用户

[GET] /v1/groups/{group_id}/members

- group_id: 群组id

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "memberList": [
      {
        "user_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
        "work_no": "0001",
        "username": "张三"
      }
    ],
    "isOwner": 1
  }
}

```



#### G7.删除群组用户

[DELETE] /v1/groups/{group_id}/members/{member_id}

- group_id: 群组id
- member_id: 成员id 

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```

### 检索模块

#### S1.搜索建议

[GET] `/v1/search/suggestions{?type,keyword,size}`

```http
GET /v1/search/suggestions?type=all&keyword=算法&size=10
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": [
    "算法",
    "优化算法",
    "算法&数学",
    "算法分析",
    "算法导论",
    "算法设计",
    "算法初级",
    "随机算法",
    "算法&数据结构",
    "算法/数据结构"
  ]
}
```

#### S2.获取高度相关的类目标签

[GET] `/v1/search/top-associations{?keyword,tag_count,category_count}`

```http
GET /v1/search/top-associations?keyword=算法&tag_count=5&category_count=5
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "tags": [
      {
        "id": 137,
        "title": "算法"
      },
      {
        "id": 2998,
        "title": "计算机"
      },
      {
        "id": 2697,
        "title": "计算机科学"
      },
      {
        "id": 66,
        "title": "哲学"
      },
      {
        "id": 133,
        "title": "编程"
      }
    ],
    "categories": [
      {
        "id": 6,
        "title": "科技"
      },
      {
        "id": 1,
        "title": "文学"
      },
      {
        "id": 2,
        "title": "流行"
      },
      {
        "id": 3,
        "title": "文化"
      },
      {
        "id": 4,
        "title": "生活"
      },
      {
        "id": 5,
        "title": "经管"
      }
    ]
  }
}
```

#### S3.搜索结果

[POST] `/v1/search/results` 

```json
{
    "type": "image",
    "keyword": "算法",
    "tags": [
       133,
       137
    ],
    "categories": [
        0,
        6
    ],
    "exts": [
       "jpg",
       "gif",
        "all"
    ],
    "created_time": {
        "from": "2010-01-01",
        "to": null
    },
    "modified_time": {
        "to": "now"
    },
    "time_zone": "+8",
    "page": 1,
    "per_page": 5
}
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "group_by_created_time": [
      {
        "key": "全部",
        "doc_count": 33,
        "from": null,
        "from_as_string": null,
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      },
      {
        "key": "三天内",
        "doc_count": 0,
        "from": "2019-07-05T16:00:00Z",
        "from_as_string": "2019-07-06",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一周内",
        "doc_count": 6,
        "from": "2019-07-01T16:00:00Z",
        "from_as_string": "2019-07-02",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一个月内",
        "doc_count": 23,
        "from": "2019-06-08T16:00:00Z",
        "from_as_string": "2019-06-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "三个月内",
        "doc_count": 25,
        "from": "2019-04-08T16:00:00Z",
        "from_as_string": "2019-04-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "半年内",
        "doc_count": 27,
        "from": "2019-01-08T16:00:00Z",
        "from_as_string": "2019-01-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年内",
        "doc_count": 27,
        "from": "2018-07-08T16:00:00Z",
        "from_as_string": "2018-07-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年前",
        "doc_count": 6,
        "from": null,
        "from_as_string": null,
        "to": "2018-07-08T16:00:00Z",
        "to_as_string": "2018-07-09"
      },
      {
        "key": "自定义",
        "doc_count": 33,
        "from": "2009-12-31T16:00:00Z",
        "from_as_string": "2010-01-01",
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      }
    ],
    "group_by_modified_time": [
      {
        "key": "全部",
        "doc_count": 33,
        "from": null,
        "from_as_string": null,
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      },
      {
        "key": "三天内",
        "doc_count": 0,
        "from": "2019-07-05T16:00:00Z",
        "from_as_string": "2019-07-06",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一周内",
        "doc_count": 15,
        "from": "2019-07-01T16:00:00Z",
        "from_as_string": "2019-07-02",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一个月内",
        "doc_count": 23,
        "from": "2019-06-08T16:00:00Z",
        "from_as_string": "2019-06-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "三个月内",
        "doc_count": 25,
        "from": "2019-04-08T16:00:00Z",
        "from_as_string": "2019-04-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "半年内",
        "doc_count": 27,
        "from": "2019-01-08T16:00:00Z",
        "from_as_string": "2019-01-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年内",
        "doc_count": 27,
        "from": "2018-07-08T16:00:00Z",
        "from_as_string": "2018-07-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年前",
        "doc_count": 6,
        "from": null,
        "from_as_string": null,
        "to": "2018-07-08T16:00:00Z",
        "to_as_string": "2018-07-09"
      },
      {
        "key": "自定义",
        "doc_count": 33,
        "from": null,
        "from_as_string": null,
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      }
    ],
    "group_by_ext": [
      {
        "key": "全部",
        "doc_count": 61
      },
      {
        "key": "png",
        "doc_count": 28
      },
      {
        "key": "gif",
        "doc_count": 17
      },
      {
        "key": "jpg",
        "doc_count": 16
      }
    ],
    "total": 33,
    "results": [
      
      {
        "ext": "jpg",
        "created_time": "2017-07-01 21:34:16",
        "derived_files": null,
        "creator": "green",
        "thumbnail": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
        "type": "image",
        "title": "算法",
        "version": 0,
        "tags": [
          6,
          133,
          137,
          2552,
          2697,
          2998,
          22409,
          24310
        ],
        "modified_time": "2017-07-06 21:34:16",
        "@timestamp": "2019-07-01T15:45:33.593Z",
        "parent_id": null,
        "@version": "1",
        "id": "image_10432347",
        "categories": [
          0,
          1,
          6
        ],
        "original_id": 10432347,
        "desc": "《算法(英文版•第4版)》作为算法领域经典的参考书，全面介绍了关于算法和数据结构的必备知识，并特别针对排序、搜索、图处理和字符串处理进行了论述。第4版具体给出了每位程序员应知应会的50个算法，提供了实际代码，而且这些Java代码实现采用了模块化的编程风格，读者可以方便地加以改造。本书配套网站提供了本书内容的摘要及更多的代码实现、测试数据、练习、教学课件等资源。《算法(英文版•第4版)》适合用作大学教材或从业者的参考书。",
        "store_key": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg"
      },
      {
        "ext": "gif",
        "created_time": "2019-06-26 21:34:16",
        "derived_files": null,
        "creator": "green",
        "thumbnail": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/26337727.jpeg",
        "type": "image",
        "title": "算法设计与分析基础",
        "version": 0,
        "tags": [
          133,
          137,
          2697,
          2998,
          22409,
          24310,
          27823
        ],
        "modified_time": "2019-07-01 21:34:16",
        "@timestamp": "2019-07-01T15:45:40.696Z",
        "parent_id": null,
        "@version": "1",
        "id": "image_26337727",
        "categories": [
          0,
          6
        ],
        "original_id": 26337727,
        "desc": "作者基于丰富的教学经验，开发了一套全新的算法分类方法。该分类法站在通用问题求解策略的高度，对现有大多数算法准确分类，从而引领读者沿着一条清晰、一致、连贯的思路来探索算法设计与分析这一迷人领域。《算法设计与分析基础(第3版)》作为第3版，相对前版调整了多个章节的内容和顺序，同时增加了一些算法，并扩展了算法的应用，使得具体算法和通用算法设计技术的对应更加清晰有序；各章累计增加了70道习题，其中包括一些有趣的谜题和面试问题。《算法设计与分析基础(第3版)》十分适合用作算法设计和分析的基础教材，也适合任何有兴趣探究算法奥秘的读者使用，只要读者具备数据结构和离散数学的知识即可。",
        "store_key": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/26337727.jpeg"
      }
    ]
  }
}
```

##### S3-1.文档全文搜索

```json
{
    "type": "doc_content",
    "keyword": "算法",
    "tags": [
       133,
       137
    ],
    "categories": [
        0,
        6
    ],
    "exts": [
       "pdf",
       "docx"
    ],
    "created_time": {
        "from": "2010-01-01",
    },
    "modified_time": {
        "to": "now"
    },
    "time_zone": "+8",
    "page": 1,
    "per_page": 5,
    "desc_highlight_count": 2,
    "content_highlight_count": 5,
    "highlight_tag": "em"
}
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "group_by_created_time": [
      {
        "key": "全部",
        "doc_count": 33,
        "from": null,
        "from_as_string": null,
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      },
      {
        "key": "三天内",
        "doc_count": 0,
        "from": "2019-07-05T16:00:00Z",
        "from_as_string": "2019-07-06",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一周内",
        "doc_count": 6,
        "from": "2019-07-01T16:00:00Z",
        "from_as_string": "2019-07-02",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一个月内",
        "doc_count": 23,
        "from": "2019-06-08T16:00:00Z",
        "from_as_string": "2019-06-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "三个月内",
        "doc_count": 25,
        "from": "2019-04-08T16:00:00Z",
        "from_as_string": "2019-04-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "半年内",
        "doc_count": 27,
        "from": "2019-01-08T16:00:00Z",
        "from_as_string": "2019-01-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年内",
        "doc_count": 27,
        "from": "2018-07-08T16:00:00Z",
        "from_as_string": "2018-07-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年前",
        "doc_count": 6,
        "from": null,
        "from_as_string": null,
        "to": "2018-07-08T16:00:00Z",
        "to_as_string": "2018-07-09"
      },
      {
        "key": "自定义",
        "doc_count": 33,
        "from": "2009-12-31T16:00:00Z",
        "from_as_string": "2010-01-01",
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      }
    ],
    "group_by_modified_time": [
      {
        "key": "全部",
        "doc_count": 33,
        "from": null,
        "from_as_string": null,
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      },
      {
        "key": "三天内",
        "doc_count": 0,
        "from": "2019-07-05T16:00:00Z",
        "from_as_string": "2019-07-06",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一周内",
        "doc_count": 15,
        "from": "2019-07-01T16:00:00Z",
        "from_as_string": "2019-07-02",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一个月内",
        "doc_count": 23,
        "from": "2019-06-08T16:00:00Z",
        "from_as_string": "2019-06-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "三个月内",
        "doc_count": 25,
        "from": "2019-04-08T16:00:00Z",
        "from_as_string": "2019-04-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "半年内",
        "doc_count": 27,
        "from": "2019-01-08T16:00:00Z",
        "from_as_string": "2019-01-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年内",
        "doc_count": 27,
        "from": "2018-07-08T16:00:00Z",
        "from_as_string": "2018-07-09",
        "to": null,
        "to_as_string": null
      },
      {
        "key": "一年前",
        "doc_count": 6,
        "from": null,
        "from_as_string": null,
        "to": "2018-07-08T16:00:00Z",
        "to_as_string": "2018-07-09"
      },
      {
        "key": "自定义",
        "doc_count": 33,
        "from": null,
        "from_as_string": null,
        "to": "2019-07-09T02:10:53.658Z",
        "to_as_string": "2019-07-09"
      }
    ],
    "group_by_ext": [
      {
        "key": "全部",
        "doc_count": 61
      },
      {
        "key": "png",
        "doc_count": 28
      },
      {
        "key": "gif",
        "doc_count": 17
      },
      {
        "key": "jpg",
        "doc_count": 16
      }
    ],
    "total": 33,
    "results": [
      {
        "ext": "jpg",
        "created_time": "2017-07-01 21:34:16",
        "derived_files": null,
        "creator": "green",
        "thumbnail": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
        "type": "image",
        "title": "算法",
        "version": 0,
        "tags": [
          6,
          133,
          137,
          2552,
          2697,
          2998,
          22409,
          24310
        ],
        "modified_time": "2017-07-06 21:34:16",
        "@timestamp": "2019-07-01T15:45:33.593Z",
        "parent_id": null,
        "@version": "1",
        "id": "image_10432347",
        "categories": [
          0,
          1,
          6
        ],
        "original_id": 10432347,
        "desc": "《算法(英文版•第4版)》作为算法领域经典的参考书，全面介绍了关于算法和数据结构的必备知识，并特别针对排序、搜索、图处理和字符串处理进行了论述。第4版具体给出了每位程序员应知应会的50个算法，提供了实际代码，而且这些Java代码实现采用了模块化的编程风格，读者可以方便地加以改造。本书配套网站提供了本书内容的摘要及更多的代码实现、测试数据、练习、教学课件等资源。《算法(英文版•第4版)》适合用作大学教材或从业者的参考书。",
        "store_key": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
         "keywords": ["<em>算法</em>", "2", "3"],
         "title_highlight": "<em>算法</em>",
         "desc_highlights": [
        	"《大学计算机教育国外著名教材系列:<em>算法</em>设计(影印版)》是近年来关于<em>算法</em>设计和分析的不可多得的优秀教材。",
           	"《大学计算机教育国外著名教材系列:<em>算法</em>设计(影印版)》围绕<em>算法</em>设计技术组织素材，对每种<em>算法</em>技术选择了多个典型范例进行分析。"
          ],
         "content_highlights": [
             "《大学计算机教育国外著名教材系列:<em>算法</em>设计(影印版)》是近年来关于<em>算法</em>设计和分析的不可多得的优秀教材。",
           	"《大学计算机教育国外著名教材系列:<em>算法</em>设计(影印版)》围绕<em>算法</em>设计技术组织素材，对每种<em>算法</em>技术选择了多个典型范例进行分析。"
         ]
      }
    ]
  }
}
```

#### S4.搜索类目或标签

[GET] `/v1/search/tags?{keyword, size}`

[GET] `/v1/search/categories?{keyword, size}`

```http
GET /v1/search/tags?keyword=算法&size=5
```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": [
      {
        "id": 6,
        "title": "科技",
		"desc": "......"
      },
      {
        "id": 1,
        "title": "文学",
        "desc": "......"
      },
      {
        "id": 2,
        "title": "流行"
      },
      {
        "id": 3,
        "title": "文化"
      },
      {
        "id": 4,
        "title": "生活"
      },
      {
        "id": 5,
        "title": "经管"
      }
	]
}
```

### 目录文档模块

#### D1.新建资源

##### D1.1 新建目录

[POST] /v1/dirs

Request Body

```json
{
	"cur_id": "e911f136-35ad-416a-b195-7b1fad4bd7f1 " // 当前所在目录id
}

```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "id": "afb502c2-d769-442b-be61-90b2a48d00a3",
    "title": "未命名",
    "thumbnail": "./assets/images/docCnt.png",
    "root": 0,
    "home": 0,
    "personal": 0,
    "modifiable": 1,
    "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "created_at": "2019-07-15T12:48:45.099+0000"
  }
}

```

##### D1.1 新建文档

[POST] /v1/docs

Request Body

```
{
    "cur_id": "e911f136-35ad-416a-b195-7b1fad4bd7f1"
}

```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "id": "3123956c-2284-40ef-9c7e-7d1982d5b9a4",
    "title": "未命名",
    "thumbnail": "./assets/images/doc.png",
    "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "created_at": "2019-07-16T12:30:41.020+0000"
  }
}

```



#### D2.获取资源元数据

##### D2.1 获取目录元数据 

[GET] /v1/dirs/{dir_id}

- dir_id: 目录id

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "id": "afb502c2-d769-442b-be61-90b2a48d00a3",
    "title": "测试目录",
    "thumbnail": "./assets/images/docCnt.png",
    "root": 0,
    "home": 0,
    "personal": 0,
    "modifiable": 1,
    "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
    "created_at": "2019-07-15T12:48:45.099+0000"
  }
}

```

##### D2.2 获取文档元数据

[GET] `/docs/{doc_id}`

```http
GET /docs/1

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": {
        "id": "1",
        "title": "code",
        "desc": "代码仓库",
        "creator": "green",
        "files": [
            "1",
            "2",
            "ABC"
        ],
        "meta_state": 1,
        "created_time": "2019-07-05 23:09:00",
        "modified_time": "2019-07-05 23:10:00"
    }
}

```

#### D3.更新资源元数据

##### D3.1 更新目录名称

[PATCH] /v1/dirs/{dir_id}

- dir_id: 目录id

Request Body

```json
{
    "title": "meeting", // 资源名称
}

```

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```

##### D3.2 更新文档元数据

[PATCH] `/docs/{doc_id}`

```http
PATCH /files/1

```

Request Body

```json
{
    "title": "code",
    "desc": "代码仓库"
}

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": {
        "id": "1",
        "title": "code",
        "desc": "代码仓库",
        "creator": "green",
        "files": [
            "1",
            "2",
            "ABC"
        ],
        "meta_state": 0,
        "created_time": "2019-07-05 23:09:00",
        "modified_time": "2019-07-05 23:10:00"
    }
}

```

#### D4.删除资源

##### D4.1 删除目录

[DELETE] /v1/dirs/{dir_id}

- dir_id: 目录id

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null   
}

```

##### D4.2 删除文档

[DELETE] /v1/docs/{dir_id}

- doc_id: 文档id

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null   
}

```

#### D5.获取下级资源

##### D5.1 获取指定目录下的子目录或文档

[GET] /v1/dirs/{dir_id}/slaves

- dir_id: 目录id

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": [
    {
      "created_time": "2019-07-16T12:06:18.398+0000",
      "thumbnail": "./assets/images/docCnt.png",
      "creator": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
      "id": "e65439e3-5d38-4a52-b700-bc28d666d098",
      "type": "dir",
      "title": "未命名"
    },
    {
      "created_time": "2019-07-16T12:17:05.634+0000",
      "thumbnail": "./assets/images/docCnt.png",
      "creator": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
      "id": "2e9e2804-cf39-4980-bc0d-be295032a721",
      "type": "dir",
      "title": "未命名"
    },
    {
      "created_time": "2019-07-16T12:30:41.020+0000",
      "thumbnail": "./assets/images/doc.png",
      "creator": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
      "id": "3123956c-2284-40ef-9c7e-7d1982d5b9a4",
      "type": "doc",
      "title": "未命名"
    }
  ]
}

```

##### D5.2 获取指定文档下的文件

### 文件模块

#### F1.获取policy（针对于使用阿里云 oss 实施的项目）

[POST] /v1/files/policy

Request Body:

```json
{
    "ext" : "", // 准备上传文件的拓展名
    "cur_id": "" //为上传到逻辑文档的 uuid
}

```

Response Body:  

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": {
        "accessKey": "", // 用户的 accessKey
        "callback": "", // 应用服务器的 /callback 接口
        "dir": "", // oss 的路径
        "expire": "", // policy 有效时间
        "host": "http://graduation-pro.oss-cn-hangzhou.aliyuncs.com", // oss 的域名
        "policy": "", 
        "signature": ""，
        "file_uuid": "" // 后面文件直传 oss 时需要的 filename
        "creator": "" //创建者id
    }
}

```

#### F2.获取签名URL（针对于使用Minio实施的项目）

[POST] /v1/files/url

Request Body:

```json
{
    "ext" : "", // 准备上传文件的拓展名
    "cur_id": "" //为上传到逻辑文档的 uuid
}

```

Response Body: 

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": {
        "url" : "" // 后续使用 PUT 方法上传文件
    }
}

```

#### F3. 文件直传 oss（针对阿里云 oss 实施）

[POST]  **host**

- 此处的 host 是获取 /policy 接口返回 body 中的 host 字段

Request Body:

```json
{
    "title": "文件名",
    "doc_id": "",  //当前上传文件所属的文档ID
    "parent_id": "",  //当前上传文件如果为某一文件的新版本，则需要传其父版本文件的ID，否则为""
    "store_key": "",  //文件在OSS中的key，由获取policy请求返回的 dir + / + 文件名组成 例：user-dir-prefix/${filename}.${suffix}
    "creator": "", // 文件的创建者
    "size": "", // 文件的大小
    "filename": "", //存储在OSS里的文件名
    "policy": "",  //获取policy请求返回的policy字段
    "accessKey": "",  //获取policy请求返回的accessid字段
    "success_action_status": 200  //回调成功返回的状态码
    "callback": "",  //获取policy请求返回的callback字段
    "signature": "",  //获取policy请求返回的signature字段
    "file": (binary)  //所上传文件的二进制文件
}

```

Response Body:  

```json
{
    "Status": "OK"
}

```

#### F4. 文件直传 oss（针对 Minio 实施）

[PUT] **host**

- 此处的 host 是获取 A2 接口返回 body 中的 url 字段

Request Body: 

```json
{
	"file": (binary)  //所上传文件的二进制文件
}

```

#### F5. 上传文件成功后创建Meta（Callback接口）

[POST] /v1/files/{id}

- id: 文件id

Request Body:

```json
{
    "title": "", //"文件名"
    "store_key": "", //文件在Minio中的key。filename可从获取签名url请求返回的url获取 例：user-dir-prefix/${filename}.${suffix}
    "doc_id": "", //当前上传文件所属的文档ID
    "parent_id": "", //当前上传文件如果为某一文件的新版本，则需要传其父版本文件的ID，否则为""
    "ext": "", // 文件后缀名
    "creator": "", // 文件的创建者
   	"size": "" // 文件的大小
}

```

Response Body:

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```

#### F6. 删除文件

[DELETE] /v1/files

Request Body:

```
{
	"file_id":["","",""] //删除的文件id
}

```

Response Body:

```json
{
	"statusCode": 200,
	"msg": "success",
	"data": null
}

```

#### F7. 下载文件 

[GET] /v1/files/​{file_id}/download

- id: 为下载文件的file_id

Response Body: HttpServletResponse

#### F8. 获取文件历史版本列表

[GET] /v1/files/​{file_id}/versions

- file_id: 为下载文件的file_id

Response Body:

```json
{
	"statusCode": 200,
	"msg": "success",
	"data": {
		"file": [
            {
                "filename": "", //OSS里的文件名
                "title": "", //文件名
                "file_thumbnail": "", //文件缩略图 (varchar) Base64编码的字符串
                "file_version": "", //文件版本号
                "parent_id": "", //父版本文件UUID
                "create_time": "", //文件创建时间
                "modified_time": "" //文件最后修改时间
            },
			{
                ...
            }
		]
	}
}

```

#### F9.获取文件meta

Retrieve File Meta

[GET] `/files/{file_id}`

```http
GET /files/1

```

Response Body

```json
{
    "id": "image_10432347",
    "title": "算法",
    "desc": "《算法(英文版•第4版)》作为算法领域经典的参考书，全面介绍了关于算法和数据结构的必备知识，并特别针对排序、搜索、图处理和字符串处理进行了论述。第4版具体给出了每位程序员应知应会的50个算法，提供了实际代码，而且这些Java代码实现采用了模块化的编程风格，读者可以方便地加以改造。本书配套网站提供了本书内容的摘要及更多的代码实现、测试数据、练习、教学课件等资源。《算法(英文版•第4版)》适合用作大学教材或从业者的参考书。",
    "creator": "green",
    "doc_id": "1",
    "type": "image",
    "ext": "jpg",
    "size": 1024,
    "categories": [
        0,
        1,
        6
    ],
    "tags": [
        6,
        133,
        137,
        2552,
        2697,
        2998,
        22409,
        24310
    ],
    "store_key": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
    "thumbnail": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
    "derived_files": [],
    "created_time": "2017-07-01 21:34:16",
    "modified_time": "2017-07-06 21:34:16",
    "version": 0,
    "original_id": "10432347",
    "parent_id": null
}

```

#### F10.更新文件meta

Update File Meta

> 部分更新，仅可更新部分字段

[PATCH] `/files/{file_id}`

```http
PATCH /files/1

```

Request Body

```json
{
    "title": "算法",
    "desc": "《算法(英文版•第4版)》作为算法领域经典的参考书，全面介绍了关于算法和数据结构的必备知识，并特别针对排序、搜索、图处理和字符串处理进行了论述。第4版具体给出了每位程序员应知应会的50个算法，提供了实际代码，而且这些Java代码实现采用了模块化的编程风格，读者可以方便地加以改造。本书配套网站提供了本书内容的摘要及更多的代码实现、测试数据、练习、教学课件等资源。《算法(英文版•第4版)》适合用作大学教材或从业者的参考书。",
    "categories": [
        0,
        1,
        6
    ],
    "tags": [
        6,
        133,
        137,
        2552,
        2697,
        2998,
        22409,
        24310
    ]
}

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": {
        "id": "image_10432347",
        "title": "算法",
        "desc": "《算法(英文版•第4版)》作为算法领域经典的参考书，全面介绍了关于算法和数据结构的必备知识，并特别针对排序、搜索、图处理和字符串处理进行了论述。第4版具体给出了每位程序员应知应会的50个算法，提供了实际代码，而且这些Java代码实现采用了模块化的编程风格，读者可以方便地加以改造。本书配套网站提供了本书内容的摘要及更多的代码实现、测试数据、练习、教学课件等资源。《算法(英文版•第4版)》适合用作大学教材或从业者的参考书。",
        "creator": "green",
        "doc_id": "1",
        "type": "image",
        "ext": "jpg",
        "size": 1024,
        "categories": [
            0,
            1,
            6
        ],
        "tags": [
            6,
            133,
            137,
            2552,
            2697,
            2998,
            22409,
            24310
        ],
        "store_key": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
        "thumbnail": "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg",
        "derived_files": [],
        "created_time": "2017-07-01 21:34:16",
        "modified_time": "2017-07-06 21:34:16",
        "version": 0,
        "original_id": "10432347",
        "parent_id": null
    }
}

```

#### F11.增加标签或类目

Create Tag / Category

[POST] `/tags/`

[POST] `/categories/`

Request Body

```json
{
    "title": "算法",
    "desc": "......"
}

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": {
        "id": 1,
        "title": "算法",
        "desc": "......"
    }
}

```

#### F12.检索标签或类目

Retrieve Tag / Category

[GET] `/tags/{tag_id}`

[GET] `/categories/{category_id}`

```http
GET /tags/1

```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
        "id": 1,
        "title": "算法",
        "desc": "......"
	}
}

```

#### F13. 更新标签或类目、

Update Tag / Category

[PUT] `/tags/{tag_id}`

[PUT] `/categories/{category_id}`

```http
PUT /tags/1

```

Request Body

```json
{
    "title": "算法",
    "desc": "......"
}

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": {
        "id": 1,
        "title": "算法",
        "desc": "......"
    }
}

```

#### F14.删除标签或类目

Delete Tag / Category

[DELETE] `/tags/{tag_id}`

[DELETE] `/categories/{category_id}`

```http
DELETE /tags/1

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": null
}

```

#### F15.获取标签或类目

Get File Tags / Categories

[GET] `/files/{file_id}/tags`

[GET] `/files/{file_id}/categories`

```http
GET /files/1/tags

```

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": [
      {
        "id": 137,
        "title": "算法",
        "desc": "......"
      },
      {
        "id": 2998,
        "title": "计算机",
		"desc": "......"
      },
      {
        "id": 2697,
        "title": "计算机科学"
      },
      {
        "id": 66,
        "title": "哲学"
      },
      {
        "id": 133,
        "title": "编程"
      }
    ]
}

```

#### F16.更新文件标签或类目

Update File Tags / Categories

[PUT] `/files/{file_id}/tags`

[PUT] `/files/{file_id}/categories`

```http
PUT /files/1/tags

```

Request Body

```json
{
    "tags": [1, 2, 3]
}

```

Response Body

```json
{
    "status": 200,
    "msg": "OK",
    "data": null
}

```

### 权限模块

#### P1.添加群组对指定目录或文档的操作权限	

[POST] /v1/resources/{resource_id}/permissions

- resource_id: 资源id

Request Body

```json
{
    "permission": "100", // 权限
    // 群组id列表
    "groups": [
        "b64b725b-ef13-4e3f-9d98-ddb3152981a6",
        "b57a392d-6510-4116-99db-f76cc16a78e5"
    ]
}

```

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```



#### P2.撤销群组对指定目录或文档的操作权限

[DELETE] /v1/resources/{resource_id}/permissions

- resource_id: 资源id

Request Body

```json
{
    "group_id": "b57a392d-6510-4116-99db-f76cc16a78e5" // 群组id
}

```

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": null
}

```

#### P3.获取对指定资源有操作权限的群组信息

[GET] /v1/resources/{resource_id}/authgroups

- resource_id: 资源id

Response Body

```json
{
  "status": 200,
  "msg": "OK",
  "data": {
    "isOwner": 1,
    "groupList": [
      {
        "permission": "100",
        "groupInfo": {
          "group_id": "75895fa6-9054-49cf-8db2-08312f1766d8",
          "group_name": "巴萨球迷",
          "group_desc": "巴萨是最菜的",
          "creator_id": "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368",
          "created_at": "2019-07-09T17:05:39.321+0000",
          "personal": 0
        }
      }
    ]
  }
}

```

#### P4.获取群组具有操作权限的所有资源信息

[GET] /v1/groups/{group_id}/authresources

- group_id: 群组id

Response Body

```json
{
    "statusCode": 200,
    "msg": "success",
    "data": {
        "resourceList": [
           {
        	"resourceInfo": {
            	"resource_id": "b64b725b-ef13-4e3f-9d98-ddb3152981a6", // 资源id
            	"resouce_name": "manager", // 资源名称
            	"desc": "", // 资源描述
            	"creator_id": "", // 创建者id
            	"created_at": "", // 创建时间
            },
            "permission": "111" // 权限
          }
    	]
    }
}

```

