package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.LcPinyinIndexSegmenter;

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


