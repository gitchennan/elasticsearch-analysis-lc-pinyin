package org.elasticsearch.indices.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinIndexTokenizer;
import org.lc.lucene.LcPinyinSearchTokenizer;

public class LcPinyinTokenizerFactory extends AbstractTokenizerFactory {

    private String analysisMode;

    private Settings settings;

    public LcPinyinTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings, String analysisMode) {
        super(indexSettings, name, settings);
        this.analysisMode = analysisMode;
        this.settings = settings;
    }

    public static LcPinyinTokenizerFactory getLcIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenizerFactory(indexSettings, env, name, settings, AnalysisSetting.index);
    }

    public static LcPinyinTokenizerFactory getLcSmartPinyinTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenizerFactory(indexSettings, env, name, settings, AnalysisSetting.search);
    }

    @Override
    public Tokenizer create() {
        if (AnalysisSetting.index.equals(analysisMode)) {
            int setting = AnalysisSetting.parseIndexAnalysisSettings(settings);
            return new LcPinyinIndexTokenizer(setting);
        } else {
            int setting = AnalysisSetting.parseSearchAnalysisSettings(settings);
            return new LcPinyinSearchTokenizer(setting);
        }
    }
}
