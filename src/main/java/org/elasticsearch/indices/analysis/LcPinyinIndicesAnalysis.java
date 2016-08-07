package org.elasticsearch.indices.analysis;

import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinAnalyzer;
import org.lc.lucene.LcPinyinIndexTokenizer;
import org.lc.lucene.LcPinyinSearchTokenizer;
import org.lc.lucene.WhitespaceFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.*;

import java.io.Reader;

public class LcPinyinIndicesAnalysis extends AbstractComponent {
    @Inject
    public LcPinyinIndicesAnalysis(final Settings settings, IndicesAnalysisService indicesAnalysisService, Environment env) {
        super(settings);

        indicesAnalysisService.analyzerProviderFactories().put("lc_index",
                new PreBuiltAnalyzerProviderFactory("lc_index", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.index)));

        indicesAnalysisService.analyzerProviderFactories().put("lc_search",
                new PreBuiltAnalyzerProviderFactory("lc_search", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.search)));


        indicesAnalysisService.tokenizerFactories().put("lc_index",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_index";
                    }

                    @Override
                    public Tokenizer create(Reader reader) {
                        return new LcPinyinIndexTokenizer(reader);
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("lc_search",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_search";
                    }

                    @Override
                    public Tokenizer create(Reader reader) {
                        return new LcPinyinSearchTokenizer(reader);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("lc_filter", new PreBuiltTokenFilterFactoryFactory(
                new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "lc_filter";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new WhitespaceFilter(tokenStream);
                    }
                }
        ));
    }
}
