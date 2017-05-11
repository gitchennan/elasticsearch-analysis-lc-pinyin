package org.elasticsearch;


import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.Assert;
import org.junit.Test;
import org.lc.core.AnalysisSetting;
import org.lc.core.DfsPinyinSeg;
import org.lc.core.PinyinDic;
import org.lc.lucene.LcPinyinAnalyzer;

import java.io.IOException;
import java.util.List;

public class LetterNumberAnalysisTest extends TestCase {
    @Test
    public void testIndexTokenizer() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.index, AnalysisSetting.IndexAnalysisSetting.full_pinyin | AnalysisSetting.IndexAnalysisSetting.first_letter);
        TokenStream tokenStream = analyzer.tokenStream("lc", "CBD123");

        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);

        tokenStream.reset();
        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "cbd");
        Assert.assertEquals(offsetAttribute.startOffset(), 0);
        Assert.assertEquals(offsetAttribute.endOffset(), 3);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "c");
        Assert.assertEquals(offsetAttribute.startOffset(), 0);
        Assert.assertEquals(offsetAttribute.endOffset(), 1);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "b");
        Assert.assertEquals(offsetAttribute.startOffset(), 1);
        Assert.assertEquals(offsetAttribute.endOffset(), 2);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "d");
        Assert.assertEquals(offsetAttribute.startOffset(), 2);
        Assert.assertEquals(offsetAttribute.endOffset(), 3);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "123");
        Assert.assertEquals(offsetAttribute.startOffset(), 3);
        Assert.assertEquals(offsetAttribute.endOffset(), 6);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "1");
        Assert.assertEquals(offsetAttribute.startOffset(), 3);
        Assert.assertEquals(offsetAttribute.endOffset(), 4);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "2");
        Assert.assertEquals(offsetAttribute.startOffset(), 4);
        Assert.assertEquals(offsetAttribute.endOffset(), 5);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "3");
        Assert.assertEquals(offsetAttribute.startOffset(), 5);
        Assert.assertEquals(offsetAttribute.endOffset(), 6);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        tokenStream.close();
    }

    @Test
    public void testSearch() throws IOException {
        LcPinyinAnalyzer analyzer = new LcPinyinAnalyzer(AnalysisSetting.search);
        TokenStream tokenStream = analyzer.tokenStream("lc", "CBD123");

        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);

        tokenStream.reset();

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "c");
        Assert.assertEquals(offsetAttribute.startOffset(), 0);
        Assert.assertEquals(offsetAttribute.endOffset(), 1);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "b");
        Assert.assertEquals(offsetAttribute.startOffset(), 1);
        Assert.assertEquals(offsetAttribute.endOffset(), 2);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "d");
        Assert.assertEquals(offsetAttribute.startOffset(), 2);
        Assert.assertEquals(offsetAttribute.endOffset(), 3);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        Assert.assertTrue(tokenStream.incrementToken());
        Assert.assertEquals(charTermAttribute.toString(), "123");
        Assert.assertEquals(offsetAttribute.startOffset(), 3);
        Assert.assertEquals(offsetAttribute.endOffset(), 6);
        Assert.assertEquals(positionIncrementAttribute.getPositionIncrement(), 1);

        tokenStream.close();
    }

}
