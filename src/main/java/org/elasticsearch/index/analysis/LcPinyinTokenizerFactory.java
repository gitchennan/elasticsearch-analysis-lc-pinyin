package org.elasticsearch.index.analysis;

import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

import java.io.Reader;

public class LcPinyinTokenizerFactory extends AbstractTokenizerFactory {

    private final Settings settings;

    @Inject
    public LcPinyinTokenizerFactory(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        this.settings = settings;
    }

    @Override
    public Tokenizer create(Reader reader) {
        String analysisMode = settings.get(AnalysisSetting.analysisMode, AnalysisSetting.full_pinyin);
        return new LcPinyinTokenizer(analysisMode, reader);
    }
}
