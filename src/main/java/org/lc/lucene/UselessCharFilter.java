package org.lc.lucene;


import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.lc.utils.CharacterUtil;

import java.io.IOException;

public class UselessCharFilter extends TokenFilter {
    private CharTermAttribute termAtt = (CharTermAttribute) this.addAttribute(CharTermAttribute.class);

    public UselessCharFilter(TokenStream in) {
        super(in);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        while (this.input.incrementToken()) {
            char[] text = this.termAtt.buffer();
            int termLength = this.termAtt.length();

            int curIdx = 0;
            char[] targetText = new char[termLength];
            for (int idx = 0; idx < termLength; idx++) {
                int charType = CharacterUtil.identifyCharType(text[idx]);
                if (charType != CharacterUtil.CHAR_USELESS) {
                    targetText[curIdx++] = text[idx];
                }
            }
            if (curIdx <= 0) {
                continue;
            }
            this.termAtt.setEmpty();
            this.termAtt.append(String.valueOf(targetText, 0, curIdx));
            return true;
        }

        return false;
    }
}
