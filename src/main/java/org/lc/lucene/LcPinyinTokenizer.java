package org.lc.lucene;

import org.lc.core.Lexeme;
import org.lc.core.LcPinyinSegmenter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.io.Reader;

public final class LcPinyinTokenizer extends Tokenizer {
    //处理模式
    private String analysisMode;
    private int skippedPositions;
    //记录最后一个词元的结束位置
    private int endPosition;
    private final LcPinyinSegmenter lcPinyinSegmenter;
    private final CharTermAttribute termAtt = this.addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = this.addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = this.addAttribute(PositionIncrementAttribute.class);

    public LcPinyinTokenizer(String analysisMode, Reader input) {
        super(input);
        this.analysisMode = analysisMode;
        lcPinyinSegmenter = new LcPinyinSegmenter(analysisMode, input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        clearAttributes();
        skippedPositions = 0;
        Lexeme lexeme = lcPinyinSegmenter.next();
        if (lexeme != null) {
            //设置词元文本
            termAtt.append(lexeme.getLexemeText());
            //设置词元长度
            termAtt.setLength(lexeme.getLength());
            //设置词元位移
            offsetAtt.setOffset(correctOffset(lexeme.beginPosition()), correctOffset(lexeme.endPosition()));
            posIncrAtt.setPositionIncrement(skippedPositions + 1);
            //记录分词的最后位置
            endPosition = lexeme.endPosition();
            return true;
        }
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        lcPinyinSegmenter.reset();
        skippedPositions = 0;
    }

    @Override
    public void end() throws IOException {
        super.end();
        // set final offset
        int finalOffset = correctOffset(endPosition);
        offsetAtt.setOffset(finalOffset, finalOffset);
        posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
    }
}


