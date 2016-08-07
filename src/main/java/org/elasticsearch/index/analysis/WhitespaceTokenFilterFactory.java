package org.elasticsearch.index.analysis;


import org.lc.lucene.WhitespaceFilter;
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

public class WhitespaceTokenFilterFactory extends AbstractTokenFilterFactory {
    @Inject
    public WhitespaceTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, String name, Settings settings) {
        super(index, indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new WhitespaceFilter(tokenStream);
    }
}
