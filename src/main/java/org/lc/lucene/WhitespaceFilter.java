package org.lc.lucene;


import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public class WhitespaceFilter extends TokenFilter {
    private CharTermAttribute termAtt = (CharTermAttribute) this.addAttribute(CharTermAttribute.class);

    public WhitespaceFilter(TokenStream in) {
        super(in);
    }

    @Override
    public boolean incrementToken() throws IOException {
        while (this.input.incrementToken()) {
            char[] text = this.termAtt.buffer();
            int termLength = this.termAtt.length();

            int curIdx = 0;
            char[] targetText = new char[termLength];
            for (int idx = 0; idx < termLength; idx++) {
                if (text[idx] != ' ') {
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
