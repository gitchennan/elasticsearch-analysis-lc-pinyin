package org.lc.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.lc.core.ISegmenter;
import org.lc.core.Lexeme;

import java.io.IOException;

public abstract class AbstractLcPinyinTokenizer extends Tokenizer {
    //记录最后一个词元的结束位置
    private int endPosition;
    private final CharTermAttribute termAtt = this.addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = this.addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = this.addAttribute(PositionIncrementAttribute.class);

    protected abstract ISegmenter getSegmenter();

    @Override
    public boolean incrementToken() throws IOException {
        clearAttributes();
        Lexeme lexeme = getSegmenter().next();
        if (lexeme != null) {
            //设置词元文本
            termAtt.append(lexeme.getLexemeText());
            //设置词元长度
            termAtt.setLength(lexeme.getLength());
            //设置词元位移
            offsetAtt.setOffset(correctOffset(lexeme.beginPosition()), correctOffset(lexeme.endPosition()));
            //词元增量
            posIncrAtt.setPositionIncrement(lexeme.getIncrementStep());
            //记录分词的最后位置
            endPosition = lexeme.endPosition();
            return true;
        }
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        getSegmenter().reset(input);
    }

    @Override
    public void end() throws IOException {
        super.end();
        // set final offset
        int finalOffset = correctOffset(endPosition);
        offsetAtt.setOffset(finalOffset, finalOffset);
        posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement());
    }
}
