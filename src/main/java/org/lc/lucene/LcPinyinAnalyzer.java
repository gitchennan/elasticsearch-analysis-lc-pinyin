package org.lc.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.Reader;

public class LcPinyinAnalyzer extends Analyzer {
    private String analysisMode;

    public LcPinyinAnalyzer(String analysisMode) {
        this.analysisMode = analysisMode;
    }

    @Override
    protected TokenStreamComponents createComponents(String name, Reader reader) {
        final Tokenizer tokenizer = new LcPinyinTokenizer(analysisMode, reader);
        return new TokenStreamComponents(tokenizer, new WhitespaceFilter(tokenizer));
    }
}
