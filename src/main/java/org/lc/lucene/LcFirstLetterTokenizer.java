package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.LcFirstLetterSegmenter;
import org.lc.core.LcPinyinSearchSegmenter;

import java.io.Reader;

public class LcFirstLetterTokenizer extends AbstractLcPinyinTokenizer {
    private final Reader input;
    private final LcFirstLetterSegmenter firstLetterSegmenter;

    public LcFirstLetterTokenizer(Reader input) {
        super(input);
        this.input = input;
        this.firstLetterSegmenter = new LcFirstLetterSegmenter(input);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return firstLetterSegmenter;
    }
}
