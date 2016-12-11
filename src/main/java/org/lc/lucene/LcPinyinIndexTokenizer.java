package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.LcPinyinIndexSegmenter;

import java.io.Reader;

public final class LcPinyinIndexTokenizer extends AbstractLcPinyinTokenizer {
    private final Reader input;
    private final LcPinyinIndexSegmenter indexSegmenter;

    public LcPinyinIndexTokenizer(Reader input, int analysisSetting) {
        super(input);
        this.input = input;
        this.indexSegmenter = new LcPinyinIndexSegmenter(input, analysisSetting);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return indexSegmenter;
    }
}


