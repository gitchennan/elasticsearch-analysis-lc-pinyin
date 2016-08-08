package org.lc.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.el.GreekLowerCaseFilter;
import org.lc.core.AnalysisSetting;

import java.io.Reader;

public class LcPinyinAnalyzer extends Analyzer {
    private String analysisMode;

    public LcPinyinAnalyzer(String analysisMode) {
        this.analysisMode = analysisMode;
    }

    @Override
    protected TokenStreamComponents createComponents(String name, Reader reader) {
        Tokenizer tokenizer = null;
        if (AnalysisSetting.search.equals(analysisMode)) {
            tokenizer = new LcPinyinSearchTokenizer(reader);
        } else {
            tokenizer = new LcPinyinIndexTokenizer(reader);
        }
        return new TokenStreamComponents(tokenizer, new GreekLowerCaseFilter(new WhitespaceFilter(tokenizer)));
    }
}
