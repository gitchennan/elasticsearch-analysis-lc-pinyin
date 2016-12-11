package org.lc.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.el.GreekLowerCaseFilter;
import org.elasticsearch.common.settings.Settings;
import org.lc.core.AnalysisSetting;

import java.io.Reader;


public class LcPinyinAnalyzer extends Analyzer {
    private String analysisMode;

    private Settings settings;

    private int analysisSetting;

    public LcPinyinAnalyzer(String analysisMode, Settings settings) {
        this.analysisMode = analysisMode;
        this.settings = settings;
    }

    public LcPinyinAnalyzer(String analysisMode) {
        this.analysisMode = analysisMode;
        analysisSetting = 0;
    }

    public LcPinyinAnalyzer(String analysisMode, int analysisSetting) {
        this.analysisMode = analysisMode;
        this.analysisSetting = analysisSetting;
    }

    @Override
    protected TokenStreamComponents createComponents(String name, Reader reader) {
        Tokenizer tokenizer = null;
        if (AnalysisSetting.index.equals(analysisMode)) {
            if(analysisSetting != 0) {
                tokenizer = new LcPinyinIndexTokenizer(reader, analysisSetting);
            } else {
                int setting = AnalysisSetting.parseIndexAnalysisSettings(settings);
                tokenizer = new LcPinyinIndexTokenizer(reader, setting);
            }
        } else {
            if (analysisSetting != 0) {
                tokenizer = new LcPinyinSearchTokenizer(reader, analysisSetting);
            } else {
                int setting = AnalysisSetting.parseSearchAnalysisSettings(settings);
                tokenizer = new LcPinyinSearchTokenizer(reader, setting);
            }
        }
        return new TokenStreamComponents(tokenizer, new LowerCaseFilter(new UselessCharFilter(tokenizer)));
    }
}
