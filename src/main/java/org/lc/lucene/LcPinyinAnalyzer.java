package org.lc.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.lc.core.AnalysisSetting;

public class LcPinyinAnalyzer extends Analyzer {
    private String analysisMode;

    public LcPinyinAnalyzer(String analysisMode) {
        this.analysisMode = analysisMode;
    }

    @Override
    protected TokenStreamComponents createComponents(String name) {
        Tokenizer tokenizer = null;
        if (AnalysisSetting.first_letter.equals(analysisMode)) {
            tokenizer = new LcFirstLetterTokenizer();
        } else if (AnalysisSetting.index.equals(analysisMode)) {
            tokenizer = new LcPinyinIndexTokenizer();
        } else {
            tokenizer = new LcPinyinSearchTokenizer();
        }
        return new TokenStreamComponents(tokenizer, new LowerCaseFilter(new UselessCharFilter(tokenizer)));
    }
}
