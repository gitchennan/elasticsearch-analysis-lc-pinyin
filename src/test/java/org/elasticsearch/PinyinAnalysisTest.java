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
        TokenStream tokenStream = analyzer.tokenStream("lc", "重庆");

        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString() + ":" + offsetAttribute.startOffset() + "," + offsetAttribute.endOffset() + ":" + positionIncrementAttribute.getPositionIncrement());
        }
        tokenStream.close();
    }

    @Test
    public void testSearch() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.search);
        TokenStream tokenStream = analyzer.tokenStream("lc", "重qing");

        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);

        tokenStream.reset();
        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "重");
        Assert.assertEquals(offsetAttribute.startOffset(), 0);
        Assert.assertEquals(offsetAttribute.endOffset(), 1);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "qing");
        Assert.assertEquals(offsetAttribute.startOffset(), 1);
        Assert.assertEquals(offsetAttribute.endOffset(), 5);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

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
