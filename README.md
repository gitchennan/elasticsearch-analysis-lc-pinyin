elasticsearch-analysis-lc-pinyin

elasticsearch.yml config:
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


index: 中文
search: 中文, 拼音, 首字母