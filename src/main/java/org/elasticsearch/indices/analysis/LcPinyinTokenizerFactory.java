package org.elasticsearch.indices.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcFirstLetterTokenizer;
import org.lc.lucene.LcPinyinIndexTokenizer;
import org.lc.lucene.LcPinyinSearchTokenizer;

public class LcPinyinTokenizerFactory extends AbstractTokenizerFactory {

    private String analysisMode;

    public LcPinyinTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings, String analysisMode) {
        super(indexSettings, name, settings);
        this.analysisMode = analysisMode;
    }

    public static LcPinyinTokenizerFactory getLcIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenizerFactory(indexSettings, env, name, settings, AnalysisSetting.index);
    }

    public static LcPinyinTokenizerFactory getLcSmartPinyinTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenizerFactory(indexSettings, env, name, settings, AnalysisSetting.search);
    }

    public static LcPinyinTokenizerFactory getLcFirstLetterTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenizerFactory(indexSettings, env, name, settings, AnalysisSetting.first_letter);
    }

    @Override
    public Tokenizer create() {
        if (AnalysisSetting.index.equals(analysisMode)) {
            return new LcPinyinIndexTokenizer();
        } else if (AnalysisSetting.first_letter.equals(analysisMode)) {
            return new LcFirstLetterTokenizer();
        } else {
            return new LcPinyinSearchTokenizer();
        }
    }
}
