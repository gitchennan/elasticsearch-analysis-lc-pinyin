package org.elasticsearch.index.analysis;

public class LcAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("lc_full_pinyin", LcPinyinTokenFilterFactory.class);
        tokenFiltersBindings.processTokenFilter("lc_first_letter", LcFirstLetterTokenFilterFactory.class);
    }

    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        analyzersBindings.processAnalyzer("lc_index", LcIndexAnalyzerProvider.class);
        analyzersBindings.processAnalyzer("lc_search", LcSearchAnalyzerProvider.class);
    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("lc_index", LcIndexTokenizerFactory.class);
        tokenizersBindings.processTokenizer("lc_search", LcSearchTokenizerFactory.class);
    }
}
