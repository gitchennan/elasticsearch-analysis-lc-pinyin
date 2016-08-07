package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.LcPinyinSearchSegmenter;

import java.io.Reader;

public class LcPinyinSearchTokenizer extends AbstractLcPinyinTokenizer {
    private final Reader input;
    private final LcPinyinSearchSegmenter searchSegmenter;

    public LcPinyinSearchTokenizer(Reader input) {
        super(input);
        this.input = input;
        this.searchSegmenter = new LcPinyinSearchSegmenter(input);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return searchSegmenter;
    }
}
