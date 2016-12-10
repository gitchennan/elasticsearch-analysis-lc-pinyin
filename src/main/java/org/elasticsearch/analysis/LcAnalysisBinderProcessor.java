package org.elasticsearch.analysis;


import org.elasticsearch.index.analysis.AnalysisModule;

public class LcAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {

    }


    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        analyzersBindings.processAnalyzer("lc_index", LcIndexAnalyzerProvider.class);
        analyzersBindings.processAnalyzer("lc_search", LcSearchAnalyzerProvider.class);
    }


    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("lc_index", LcIndexTokenizerFactory.class);
        tokenizersBindings.processTokenizer("lc_search", LcIndexTokenizerFactory.class);
    }
}
