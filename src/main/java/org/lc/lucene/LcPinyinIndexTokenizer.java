package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.Lexeme;
import org.lc.core.LcPinyinIndexSegmenter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.io.Reader;

public final class LcPinyinIndexTokenizer extends AbstractLcPinyinTokenizer {
    private final LcPinyinIndexSegmenter indexSegmenter;
    public LcPinyinIndexTokenizer() {
        super();
        this.indexSegmenter = new LcPinyinIndexSegmenter(input);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return indexSegmenter;
    }
}


