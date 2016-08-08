package org.elasticsearch;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.Test;
import org.lc.core.AnalysisSetting;
import org.lc.core.PinyinDic;
import org.lc.lucene.LcPinyinAnalyzer;

import java.io.IOException;

public class PinyinAnalysisTest extends TestCase {
    @Test
    public void testTokenizer() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.index);
        TokenStream tokenStream = analyzer.tokenStream("lc", "基huige");
        tokenStream.reset();

        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
            PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);
            System.out.println(charTermAttribute.toString() + ":" + offsetAttribute.startOffset() + "," + offsetAttribute.endOffset() + ":" + positionIncrementAttribute.getPositionIncrement());

        }
        tokenStream.close();
    }

    @Test
    public void testSearch() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.search);
        TokenStream tokenStream = analyzer.tokenStream("lc", "中华重庆大学生188号");
        tokenStream.reset();

        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
            PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);
            System.out.println(charTermAttribute.toString() + ":" + offsetAttribute.startOffset() + "," + offsetAttribute.endOffset() + ":" + positionIncrementAttribute.getPositionIncrement());

        }
        tokenStream.close();
    }

    @Test
    public void testLoadPinyinDic() throws IOException {
        PinyinDic pinyinDic = PinyinDic.getInstance();
        Assert.assertTrue(pinyinDic.getInstance().contains("chen"));
        Assert.assertTrue(pinyinDic.getInstance().contains("chen"));
        Assert.assertFalse(pinyinDic.getInstance().contains("abc"));
    }
}
