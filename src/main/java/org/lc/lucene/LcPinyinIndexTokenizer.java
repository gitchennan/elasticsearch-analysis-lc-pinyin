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
    private final Reader input;
    private final LcPinyinIndexSegmenter indexSegmenter;
    public LcPinyinIndexTokenizer(Reader input) {
        super(input);
        this.input = input;
        this.indexSegmenter = new LcPinyinIndexSegmenter(input);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return indexSegmenter;
    }
}


