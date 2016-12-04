package org.elasticsearch.indices.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.lc.core.PinyinFilterSetting;
import org.lc.lucene.LcPinyinTokenFilter;

public class LcPinyinTokenFilterFactory extends AbstractTokenFilterFactory {

    private String filterMode;

    public LcPinyinTokenFilterFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings, String filterMode) {
        super(indexSettings, name, settings);
        this.filterMode = filterMode;

    }

    public static LcPinyinTokenFilterFactory getLcFirstLetterTokenFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenFilterFactory(indexSettings, env, name, settings, PinyinFilterSetting.first_letter);
    }

    public static LcPinyinTokenFilterFactory getLcFullPinyinTokenFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new LcPinyinTokenFilterFactory(indexSettings, env, name, settings, PinyinFilterSetting.full_pinyin);
    }


    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new LcPinyinTokenFilter(tokenStream, filterMode);
    }
}
