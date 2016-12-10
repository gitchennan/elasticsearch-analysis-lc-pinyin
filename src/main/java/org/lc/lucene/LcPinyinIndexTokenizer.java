package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.LcPinyinIndexSegmenter;

public final class LcPinyinIndexTokenizer extends AbstractLcPinyinTokenizer {

    private final LcPinyinIndexSegmenter indexSegmenter;

    public LcPinyinIndexTokenizer(int analysisSetting) {
        this.indexSegmenter = new LcPinyinIndexSegmenter(input, analysisSetting);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return indexSegmenter;
    }
}


