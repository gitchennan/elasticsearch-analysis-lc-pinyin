elasticsearch-analysis-lc-pinyin

elasticsearch.yml config:
index:
  analysis:
    analyzer:
      lc:
         alias: [lc_analyzer]
         type: org.elasticsearch.index.analysis.LcPinyinAnalyzerProvider
      lc_pinyin:
         type: lc
         analysisMode: full_pinyin
      lc_first_letter:
         type: lc
         analysisMode: first_letter_only


test analyzer:
curl -XGET 'localhost:9200/judge_v2/_analyze?analyzer=lc&pretty' -d '湖北工业大学'
{
  "tokens" : [ {
    "token" : "hu",
    "start_offset" : 0,
    "end_offset" : 1,
    "type" : "word",
    "position" : 1
  }, {
    "token" : "bei",
    "start_offset" : 1,
    "end_offset" : 2,
    "type" : "word",
    "position" : 2
  }, {
    "token" : "gong",
    "start_offset" : 2,
    "end_offset" : 3,
    "type" : "word",
    "position" : 3
  }, {
    "token" : "ye",
    "start_offset" : 3,
    "end_offset" : 4,
    "type" : "word",
    "position" : 4
  }, {
    "token" : "da",
    "start_offset" : 4,
    "end_offset" : 5,
    "type" : "word",
    "position" : 5
  }, {
    "token" : "dai",
    "start_offset" : 4,
    "end_offset" : 5,
    "type" : "word",
    "position" : 6
  }, {
    "token" : "xue",
    "start_offset" : 5,
    "end_offset" : 6,
    "type" : "word",
    "position" : 7
  } ]
}
