package org.elasticsearch;

import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.Test;
import org.lc.core.AnalysisSetting;
import org.lc.core.DfsPinyinSeg;
import org.lc.core.PinyinDic;
import org.lc.lucene.LcPinyinAnalyzer;

import java.io.IOException;
import java.util.List;


public class PinyinAnalysisTest extends TestCase {
    @Test
    public void testIndexTokenizer() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.index, AnalysisSetting.IndexAnalysisSetting.full_pinyin | AnalysisSetting.IndexAnalysisSetting.first_letter);
        TokenStream tokenStream = analyzer.tokenStream("lc", "重庆");

        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);

        tokenStream.reset();
        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "zhong");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 0);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 1);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "z");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 0);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 1);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 0);

        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "chong");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 0);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 1);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 0);

        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "c");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 0);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 1);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 0);

        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "qing");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 1);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 2);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "q");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 1);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 2);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 0);

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
        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "重");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 0);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 1);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "qing");
        org.junit.Assert.assertEquals(offsetAttribute.startOffset(), 1);
        org.junit.Assert.assertEquals(offsetAttribute.endOffset(), 5);
        org.junit.Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        tokenStream.close();
    }

    @Test
    public void testFirstLetterAnalysis() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.search, AnalysisSetting.SearchAnalysisSetting.single_letter);
        TokenStream tokenStream = analyzer.tokenStream("lc", "hg");
        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "h");
        org.junit.Assert.assertTrue(tokenStream.incrementToken());
        org.junit.Assert.assertEquals(charTermAttribute.toString(), "g");
        tokenStream.close();
    }

    @Test
    public void testLoadPinyinDic() throws IOException {
        PinyinDic pinyinDic = PinyinDic.getInstance();
        org.junit.Assert.assertTrue(pinyinDic.getInstance().contains("chen"));
        org.junit.Assert.assertTrue(pinyinDic.getInstance().contains("chen"));
        org.junit.Assert.assertFalse(pinyinDic.getInstance().contains("abc"));
    }

    @Test
    public void testDfsSeg() throws Exception {
        String text = "xinyongA";
        DfsPinyinSeg dfsPinyinSeg = new DfsPinyinSeg(text);
        List<String> tokens = dfsPinyinSeg.segDeepSearch();

        org.junit.Assert.assertNotNull(tokens);
        org.junit.Assert.assertTrue(tokens.size() == 3);
        org.junit.Assert.assertEquals(tokens.get(0), "xin");
        org.junit.Assert.assertEquals(tokens.get(1), "yong");
        org.junit.Assert.assertEquals(tokens.get(2), "A");

        text = "hongen";
        dfsPinyinSeg = new DfsPinyinSeg(text);
        tokens = dfsPinyinSeg.segDeepSearch();

        org.junit.Assert.assertNotNull(tokens);
        org.junit.Assert.assertTrue(tokens.size() == 2);
        org.junit.Assert.assertEquals(tokens.get(0), "hong");
        org.junit.Assert.assertEquals(tokens.get(1), "en");
    }
}
