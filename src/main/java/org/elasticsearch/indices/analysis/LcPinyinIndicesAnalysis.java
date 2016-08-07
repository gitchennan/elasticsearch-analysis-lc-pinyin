package org.elasticsearch.indices.analysis;

import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinAnalyzer;
import org.lc.lucene.LcPinyinTokenizer;
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

        final String analysisMode = settings.get("analysisMode", AnalysisSetting.full_pinyin);

        indicesAnalysisService.analyzerProviderFactories().put("lc",
                new PreBuiltAnalyzerProviderFactory("lc", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(analysisMode)));

        indicesAnalysisService.analyzerProviderFactories().put("lc_pinyin",
                new PreBuiltAnalyzerProviderFactory("lc_pinyin", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.full_pinyin)));

        indicesAnalysisService.analyzerProviderFactories().put("lc_first_letter",
                new PreBuiltAnalyzerProviderFactory("lc_first_letter", AnalyzerScope.GLOBAL,
                        new LcPinyinAnalyzer(AnalysisSetting.first_letter_only)));

        indicesAnalysisService.tokenizerFactories().put("lc",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc";
                    }

                    @Override
                    public Tokenizer create(Reader reader) {
                        return new LcPinyinTokenizer(analysisMode, reader);
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("lc_pinyin",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_pinyin";
                    }

                    @Override
                    public Tokenizer create(Reader reader) {
                        return new LcPinyinTokenizer(AnalysisSetting.full_pinyin, reader);
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("lc_first_letter",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "lc_first_letter";
                    }

                    @Override
                    public Tokenizer create(Reader reader) {
                        return new LcPinyinTokenizer(AnalysisSetting.first_letter_only, reader);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("lc", new PreBuiltTokenFilterFactoryFactory(
                new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "lc";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new WhitespaceFilter(tokenStream);
                    }
                }
        ));
    }
}
