package org.elasticsearch.index.analysis;

import org.lc.core.AnalysisSetting;
import org.lc.lucene.LcPinyinAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

public class LcPinyinAnalyzerProvider extends AbstractIndexAnalyzerProvider<LcPinyinAnalyzer> {

    private final LcPinyinAnalyzer analyzer;

    @Inject
    public LcPinyinAnalyzerProvider(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        String analysisMode = settings.get(AnalysisSetting.analysisMode, AnalysisSetting.index);
        analyzer = new LcPinyinAnalyzer(analysisMode);
    }

    public LcPinyinAnalyzer get() {
        return analyzer;
    }
}
