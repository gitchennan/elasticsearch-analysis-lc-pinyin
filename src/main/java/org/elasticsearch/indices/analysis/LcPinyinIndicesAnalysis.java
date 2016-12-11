package org.elasticsearch.indices.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.*;
import org.lc.core.AnalysisSetting;
import org.lc.core.PinyinFilterSetting;
import org.lc.lucene.LcPinyinAnalyzer;
import org.lc.lucene.LcPinyinIndexTokenizer;
import org.lc.lucene.LcPinyinSearchTokenizer;
import org.lc.lucene.LcPinyinTokenFilter;

public class LcPinyinIndicesAnalysis extends AbstractComponent {
    @Inject
    public LcPinyinIndicesAnalysis(final Settings settings, IndicesAnalysisService indicesAnalysisService, Environment env) {
        super(settings);

        indicesAnalysisService.analyzerProviderFactories().put("lc_index",
                new PreBuiltAnalyzerProviderFactory("lc_index", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.index, settings)));

        indicesAnalysisService.analyzerProviderFactories().put("lc_search",
                new PreBuiltAnalyzerProviderFactory("lc_search", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.search, settings)));


        indicesAnalysisService.tokenizerFactories().put("lc_index",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_index";
                    }

                    @Override
                    public Tokenizer create() {
                        int setting = AnalysisSetting.parseIndexAnalysisSettings(settings);
                        return new LcPinyinIndexTokenizer(setting);
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
                        int setting = AnalysisSetting.parseSearchAnalysisSettings(settings);
                        return new LcPinyinSearchTokenizer(setting);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("lc_full_pinyin", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "lc_full_pinyin";
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new LcPinyinTokenFilter(tokenStream, PinyinFilterSetting.full_pinyin);
            }
        }));

        indicesAnalysisService.tokenFilterFactories().put("lc_first_letter", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "lc_first_letter";
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new LcPinyinTokenFilter(tokenStream, PinyinFilterSetting.first_letter);
            }
        }));
    }
}
