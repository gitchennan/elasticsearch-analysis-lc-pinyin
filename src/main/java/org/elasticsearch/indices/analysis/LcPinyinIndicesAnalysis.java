package org.elasticsearch.indices.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcFirstLetterTokenizer;
import org.lc.lucene.LcPinyinAnalyzer;
import org.lc.lucene.LcPinyinIndexTokenizer;
import org.lc.lucene.LcPinyinSearchTokenizer;

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

        indicesAnalysisService.analyzerProviderFactories().put("lc_first_letter",
                new PreBuiltAnalyzerProviderFactory("lc_first_letter", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.first_letter)));


        indicesAnalysisService.tokenizerFactories().put("lc_index",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_index";
                    }

                    @Override
                    public Tokenizer create() {
                        return new LcPinyinIndexTokenizer();
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("lc_search",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_search";
                    }

                    @Override
                    public Tokenizer create() {
                        return new LcPinyinSearchTokenizer();
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("lc_first_letter",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_first_letter";
                    }

                    @Override
                    public Tokenizer create() {
                        return new LcFirstLetterTokenizer();
                    }
                }));
    }
}
