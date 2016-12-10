package org.elasticsearch;

import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.lc.core.AnalysisSetting;
import org.lc.core.PinyinFilterSetting;
import org.lc.lucene.LcPinyinAnalyzer;
import org.lc.lucene.LcPinyinTokenFilter;

import java.io.IOException;

public class PinyinFilterTest extends TestCase {
    public void testFullPinyinFilter() throws IOException {

        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.search);
        TokenStream tokenStream = analyzer.tokenStream("lc", "作者 : 陈楠");

        LcPinyinTokenFilter lcPinyinTokenFilter = new LcPinyinTokenFilter(tokenStream, PinyinFilterSetting.full_pinyin);

        CharTermAttribute charTermAttribute = lcPinyinTokenFilter.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = lcPinyinTokenFilter.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = lcPinyinTokenFilter.getAttribute(PositionIncrementAttribute.class);

        lcPinyinTokenFilter.reset();
        while (lcPinyinTokenFilter.incrementToken()) {
            System.out.println(charTermAttribute.toString() + ":" + offsetAttribute.startOffset() + "," + offsetAttribute.endOffset() + ":" + positionIncrementAttribute.getPositionIncrement());
        }
        lcPinyinTokenFilter.close();
    }

    public void testFirstLetterFilter() throws IOException {

        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.search);
        TokenStream tokenStream = analyzer.tokenStream("lc", "作者 : 陈楠");

        LcPinyinTokenFilter lcPinyinTokenFilter = new LcPinyinTokenFilter(tokenStream, PinyinFilterSetting.first_letter);

        CharTermAttribute charTermAttribute = lcPinyinTokenFilter.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = lcPinyinTokenFilter.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = lcPinyinTokenFilter.getAttribute(PositionIncrementAttribute.class);

        lcPinyinTokenFilter.reset();
        while (lcPinyinTokenFilter.incrementToken()) {
            System.out.println(charTermAttribute.toString() + ":" + offsetAttribute.startOffset() + "," + offsetAttribute.endOffset() + ":" + positionIncrementAttribute.getPositionIncrement());
        }
        lcPinyinTokenFilter.close();
    }
}
