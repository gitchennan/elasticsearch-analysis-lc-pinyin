package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.lc.core.PinyinFilterSetting;
import org.lc.lucene.LcPinyinTokenFilter;

public class LcFirstLetterTokenFilterFactory extends AbstractTokenFilterFactory {

    @Inject
    public LcFirstLetterTokenFilterFactory(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new LcPinyinTokenFilter(tokenStream, PinyinFilterSetting.first_letter);
    }
}
