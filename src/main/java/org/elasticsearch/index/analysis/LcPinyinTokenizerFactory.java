package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.lc.lucene.LcPinyinIndexTokenizer;

import java.io.Reader;

public class LcPinyinTokenizerFactory extends AbstractTokenizerFactory {
    @Inject
    public LcPinyinTokenizerFactory(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
    }

    @Override
    public Tokenizer create(Reader reader) {
        return new LcPinyinIndexTokenizer(reader);
    }
}
