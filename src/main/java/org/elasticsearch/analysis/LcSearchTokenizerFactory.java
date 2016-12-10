package org.elasticsearch.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinSearchTokenizer;

public class LcSearchTokenizerFactory extends AbstractTokenizerFactory {

    private Settings settings;

    @Inject
    public LcSearchTokenizerFactory(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        this.settings = settings;
    }

    @Override
    public Tokenizer create() {
        int setting = AnalysisSetting.parseIndexAnalysisSettings(settings);
        return new LcPinyinSearchTokenizer(setting);
    }
}
