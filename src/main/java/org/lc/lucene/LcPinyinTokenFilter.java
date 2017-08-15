package org.lc.lucene;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.lc.core.PinyinFilterSetting;
import org.lc.utils.CharacterUtil;
import org.lc.utils.PinyinHelper;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LcPinyinTokenFilter extends TokenFilter {

    private CharTermAttribute termAtt = (CharTermAttribute) this.addAttribute(CharTermAttribute.class);

    private LinkedList<String> tokensCache = new LinkedList<String>();

    private String filterMode;

    public LcPinyinTokenFilter(TokenStream input, String filterMode) {
        super(input);
        this.filterMode = filterMode;
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (hasMoreTokenInCache()) {
            this.termAtt.setEmpty();
            this.termAtt.append(nextTokenLexeme());
            return true;
        }
        if (this.input.incrementToken()) {
            char[] text = this.termAtt.buffer();
            int termLength = this.termAtt.length();
            List<String> pinyinTokens = convertChineseTokenToPinyin(text, termLength);

            Iterator<String> pinyinIterator = pinyinTokens.iterator();
            if (pinyinIterator.hasNext()) {
                String pinyinItem = pinyinIterator.next();
                while (pinyinIterator.hasNext()) {
                    addTokenToCache(pinyinIterator.next());
                }
                this.termAtt.setEmpty();
                this.termAtt.append(pinyinItem);
            }
            return true;
        }
        return false;
    }

    private List<String> convertChineseTokenToPinyin(char[] text, int length) {
        LinkedList<String> pinyinList = new LinkedList<String>();
        dfsConvert(text, length, 0, new StringBuilder(), pinyinList);
        return pinyinList;
    }

    private void dfsConvert(char[] text, int length, int depth, StringBuilder pinyinBuffer, Collection<String> collector) {
        if (depth == length) {
            if (pinyinBuffer.length() > 0) {
                collector.add(pinyinBuffer.toString());
            }
            return;
        }
        char ch = text[depth];
        String[] pinyinItemList = convert(ch);
        for (int idx = 0; idx < pinyinItemList.length; idx++) {
            pinyinBuffer.append(pinyinItemList[idx]);
            dfsConvert(text, length, depth + 1, pinyinBuffer, collector);
            pinyinBuffer.delete(pinyinBuffer.length() - pinyinItemList[idx].length(), pinyinBuffer.length());
        }
    }

    private String[] convert(char ch) {
        int charType = CharacterUtil.identifyCharType(ch);
        if (CharacterUtil.CHAR_CHINESE == charType) {
            String[] pinyinList = PinyinHelper.convertChineseToPinyin(ch, false);
            if (PinyinFilterSetting.first_letter.equalsIgnoreCase(filterMode)) {
                for (int idx = 0; idx < pinyinList.length; idx++) {
                    pinyinList[idx] = String.valueOf(pinyinList[idx].charAt(0));
                }
            }
            return pinyinList;
        }
        return new String[]{String.valueOf(ch)};
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        tokensCache.clear();
    }

    protected boolean hasMoreTokenInCache() {
        return !tokensCache.isEmpty();
    }

    private String nextTokenLexeme() {
        return tokensCache.pollFirst();
    }

    private void addTokenToCache(String token) {
        if (token != null) {
            tokensCache.add(token);
        }
    }
}
