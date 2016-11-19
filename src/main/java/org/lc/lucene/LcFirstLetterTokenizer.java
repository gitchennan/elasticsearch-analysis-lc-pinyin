package org.lc.lucene;

import org.lc.core.ISegmenter;
import org.lc.core.LcFirstLetterSegmenter;

public class LcFirstLetterTokenizer extends AbstractLcPinyinTokenizer {
    private final LcFirstLetterSegmenter firstLetterSegmenter;

    public LcFirstLetterTokenizer() {
        super();
        this.firstLetterSegmenter = new LcFirstLetterSegmenter(input);
    }

    @Override
    protected ISegmenter getSegmenter() {
        return firstLetterSegmenter;
    }
}
