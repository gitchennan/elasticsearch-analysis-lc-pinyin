package org.elasticsearch.indices.analysis;


import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinAnalyzer;

public class LcPinyinAnalyzerProvider extends AbstractIndexAnalyzerProvider<LcPinyinAnalyzer> {
    private final LcPinyinAnalyzer analyzer;

    public LcPinyinAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings, String analysisMode) {
        super(indexSettings, name, settings);
        analyzer = new LcPinyinAnalyzer(analysisMode, settings);
    }

    public static LcPinyinAnalyzerProvider getIndexAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinAnalyzerProvider(indexSettings, env, name, settings, AnalysisSetting.index);
    }

    public static LcPinyinAnalyzerProvider getSmartPinyinAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinAnalyzerProvider(indexSettings, env, name, settings, AnalysisSetting.search);
    }

    @Override
    public LcPinyinAnalyzer get() {
        return this.analyzer;
    }
}
