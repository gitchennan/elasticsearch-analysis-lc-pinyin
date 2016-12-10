package org.elasticsearch.plugin.analysis.lc;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.indices.analysis.LcPinyinAnalyzerProvider;
import org.elasticsearch.indices.analysis.LcPinyinTokenFilterFactory;
import org.elasticsearch.indices.analysis.LcPinyinTokenizerFactory;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

public class AnalysisLcPinyinPlugin extends Plugin implements AnalysisPlugin {

    public static String PLUGIN_NAME = "analysis-lc-pinyin";

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> providerMap
                = new HashMap<String, AnalysisModule.AnalysisProvider<TokenizerFactory>>();

        providerMap.put("lc_index", LcPinyinTokenizerFactory::getLcIndexTokenizerFactory);
        providerMap.put("lc_search", LcPinyinTokenizerFactory::getLcSmartPinyinTokenizerFactory);

        return providerMap;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> providerMap
                = new HashMap<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>>();

        providerMap.put("lc_index", LcPinyinAnalyzerProvider::getIndexAnalyzerProvider);
        providerMap.put("lc_search", LcPinyinAnalyzerProvider::getSmartPinyinAnalyzerProvider);

        return providerMap;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<org.elasticsearch.index.analysis.TokenFilterFactory>> getTokenFilters() {
        Map<String, AnalysisModule.AnalysisProvider<org.elasticsearch.index.analysis.TokenFilterFactory>> extra = new HashMap<>();
        extra.put("lc_full_pinyin", LcPinyinTokenFilterFactory::getLcFullPinyinTokenFilterFactory);
        extra.put("lc_first_letter", LcPinyinTokenFilterFactory::getLcFirstLetterTokenFilterFactory);
        return extra;
    }

}
