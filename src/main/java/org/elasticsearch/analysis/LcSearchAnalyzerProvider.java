package org.elasticsearch.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinAnalyzer;

public class LcSearchAnalyzerProvider extends AbstractIndexAnalyzerProvider<LcPinyinAnalyzer> {

    private final LcPinyinAnalyzer analyzer;

    @Inject
    public LcSearchAnalyzerProvider(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        analyzer = new LcPinyinAnalyzer(AnalysisSetting.search, settings);
    }

    @Override
    public LcPinyinAnalyzer get() {
        return this.analyzer;
    }
}
