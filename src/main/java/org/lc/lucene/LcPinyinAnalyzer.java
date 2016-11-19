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
        if (AnalysisSetting.index.equals(analysisMode)) {
            tokenizer = new LcPinyinIndexTokenizer(reader);
        } else if (AnalysisSetting.first_letter.equals(analysisMode)) {
            tokenizer = new LcFirstLetterTokenizer(reader);
        } else {
            tokenizer = new LcPinyinSearchTokenizer(reader);
        }
        return new TokenStreamComponents(tokenizer, new GreekLowerCaseFilter(new UselessCharFilter(tokenizer)));
    }
}
