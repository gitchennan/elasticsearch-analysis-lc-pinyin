package org.elasticsearch.index.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinAnalyzer;

public class LcIndexAnalyzerProvider extends AbstractIndexAnalyzerProvider<LcPinyinAnalyzer> {

    private final LcPinyinAnalyzer analyzer;

    @Inject
    public LcIndexAnalyzerProvider(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        analyzer = new LcPinyinAnalyzer(AnalysisSetting.index, settings);
    }

    @Override
    public LcPinyinAnalyzer get() {
        return this.analyzer;
    }
}
