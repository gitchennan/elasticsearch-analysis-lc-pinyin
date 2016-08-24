LC Analysis for Elasticsearch
=============================

Versions
--------

LC version | ES version
-----------|-----------
2.2.2 -> master | 2.2.2
1.4.5 | 1.4.5


Install
-------

elasticsearch.yml 配置如下(ES2.0以上不需要下面配置):

<pre>
index:
  analysis:
    analyzer:
      lc:
         alias: [lc_analyzer]
         type: org.elasticsearch.index.analysis.LcPinyinAnalyzerProvider
      lc_index:
         type: lc
         analysisMode: index
      lc_search:
         type: lc
         analysisMode: search
</pre>

lc_index: 这个分词器用于做索引时指定
lc_search: 这个分词器用于查询时使用


#### Quick Example

1.create a index

```bash
curl -XPUT http://localhost:9200/index
```

2.create a mapping

```bash
curl -XPOST http://localhost:9200/index/fulltext/_mapping -d'
{
    "fulltext": {
             "_all": {
            "index_analyzer": "lc_index",
            "search_analyzer": "lc_search",
            "term_vector": "no",
            "store": "false"
        },
        "properties": {
            "content": {
                "type": "string",
                "store": "no",
                "term_vector": "with_positions_offsets",
                "index_analyzer": "lc_index",
                "search_analyzer": "lc_search",
                "include_in_all": "true",
                "boost": 8
            }
        }
    }
}'
```

3.index some docs

```bash
curl -XPOST http://localhost:9200/index/fulltext/1 -d'
{"content":"美国留给伊拉克的是个烂摊子吗"}
'
```

```bash
curl -XPOST http://localhost:9200/index/fulltext/2 -d'
{"content":"公安部：各地校车将享最高路权"}
'
```

```bash
curl -XPOST http://localhost:9200/index/fulltext/3 -d'
{"content":"中韩渔警冲突调查：韩警平均每天扣1艘中国渔船"}
'
```

```bash
curl -XPOST http://localhost:9200/index/fulltext/4 -d'
{"content":"中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"}
'
```

4.query with highlighting

```bash
curl -XPOST http://localhost:9200/index/fulltext/_search  -d'
{
    "query": {
        "match": {
          "content": {
            "query": "中国",
            "analyzer": "lc_search",
            "type": "phrase",
            "slop": 1,
            "zero_terms_query": "NONE"
          }
        }
    },
    "highlight" : {
        "pre_tags" : ["<tag1>", "<tag2>"],
        "post_tags" : ["</tag1>", "</tag2>"],
        "fields" : {
            "content" : {}
        }
    }
}
'
```
Result

```json
{
	"took": 8,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"failed": 0
	},
	"hits": {
		"total": 2,
		"max_score": 0.92055845,
		"hits": [{
			"_index": "index",
			"_type": "fulltext",
			"_id": "4",
			"_score": 0.92055845,
			"_source": {
				"content": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
			},
			"highlight": {
				"content": ["<tag1>中国</tag1>驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"]
			}
		},
		{
			"_index": "index",
			"_type": "fulltext",
			"_id": "3",
			"_score": 0.92055845,
			"_source": {
				"content": "中韩渔警冲突调查：韩警平均每天扣1艘中国渔船"
			},
			"highlight": {
				"content": ["中韩渔警冲突调查：韩警平均每天扣1艘<tag1>中国</tag1>渔船"]
			}
		}]
	}
}
```


```bash
curl -XPOST http://localhost:9200/index/fulltext/_search  -d'
{
  "query": {
    "match": {
      "content": {
        "query": "lsj",
        "analyzer": "lc_search",
        "type": "phrase",
        "slop": 1,
        "zero_terms_query": "NONE"
      }
    }
  },
  "highlight": {
    "pre_tags": [
      "<tag1>",
      "<tag2>"
    ],
    "post_tags": [
      "</tag1>",
      "</tag2>"
    ],
    "fields": {
      "content": {}
    }
  }
}
'
```

```json
{
	"took": 8,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"failed": 0
	},
	"hits": {
		"total": 1,
		"max_score": 2.8808377,
		"hits": [{
			"_index": "index",
			"_type": "fulltext",
			"_id": "4",
			"_score": 2.8808377,
			"_source": {
				"content": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
			},
			"highlight": {
				"content": ["中国驻<tag2>洛杉矶</tag2>领事馆遭亚裔男子枪击 嫌犯已自首"]
			}
		}]
	}
}
```

```bash
curl -XPOST http://localhost:9200/index/fulltext/_search  -d'
{
  "query": {
    "match": {
      "content": {
        "query": "luoshanji",
        "analyzer": "lc_search",
        "type": "phrase",
        "slop": 1,
        "zero_terms_query": "NONE"
      }
    }
  },
  "highlight": {
    "pre_tags": [
      "<tag1>",
      "<tag2>"
    ],
    "post_tags": [
      "</tag1>",
      "</tag2>"
    ],
    "fields": {
      "content": {}
    }
  }
}
'
```

```json
{
	"took": 10,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"failed": 0
	},
	"hits": {
		"total": 1,
		"max_score": 2.8808377,
		"hits": [{
			"_index": "index",
			"_type": "fulltext",
			"_id": "4",
			"_score": 2.8808377,
			"_source": {
				"content": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
			},
			"highlight": {
				"content": ["中国驻<tag2>洛杉矶</tag2>领事馆遭亚裔男子枪击 嫌犯已自首"]
			}
		}]
	}
}
```


```bash
curl -XPOST http://localhost:9200/index/fulltext/_search  -d'
{
  "query": {
    "match": {
      "content": {
        "query": "领shig",
        "analyzer": "lc_search",
        "type": "phrase",
        "slop": 1,
        "zero_terms_query": "NONE"
      }
    }
  },
  "highlight": {
    "pre_tags": [
      "<tag1>",
      "<tag2>"
    ],
    "post_tags": [
      "</tag1>",
      "</tag2>"
    ],
    "fields": {
      "content": {}
    }
  }
}
'
```

```json
{
	"took": 14,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"failed": 0
	},
	"hits": {
		"total": 1,
		"max_score": 2.8808377,
		"hits": [{
			"_index": "index",
			"_type": "fulltext",
			"_id": "4",
			"_score": 2.8808377,
			"_source": {
				"content": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
			},
			"highlight": {
				"content": ["中国驻洛杉矶<tag2>领事馆</tag2>遭亚裔男子枪击 嫌犯已自首"]
			}
		}]
	}
}
```


```bash
curl -XPOST http://localhost:9200/index/fulltext/_search  -d'
{
  "query": {
    "match": {
      "content": {
        "query": "男zi枪j",
        "analyzer": "lc_search",
        "type": "phrase",
        "slop": 1,
        "zero_terms_query": "NONE"
      }
    }
  },
  "highlight": {
    "pre_tags": [
      "<tag1>",
      "<tag2>"
    ],
    "post_tags": [
      "</tag1>",
      "</tag2>"
    ],
    "fields": {
      "content": {}
    }
  }
}
'
```

```json
{
	"took": 6,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"failed": 0
	},
	"hits": {
		"total": 1,
		"max_score": 1.8411169,
		"hits": [{
			"_index": "index",
			"_type": "fulltext",
			"_id": "4",
			"_score": 1.8411169,
			"_source": {
				"content": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
			},
			"highlight": {
				"content": ["中国驻洛杉矶领事馆遭亚裔<tag1>男子枪击</tag1> 嫌犯已自首"]
			}
		}]
	}
}
```