package org.lc.core;


import org.lc.utils.CharacterUtil;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class LcFirstLetterSegmenter extends AbstractPinyinSegmenter {

    public LcFirstLetterSegmenter(Reader input) {
        super(input);
    }

    @Override
    protected List<Lexeme> processTokenToLexeme(String token) {
        List<Lexeme> lexemeList = new LinkedList<Lexeme>();
        char ch = token.charAt(0);
        if (CharacterUtil.isLetter(ch)) {
            List<String> pinyinList = getLetters(token);
            if (pinyinList.size() > 0) {
                for (String pinyinItem : pinyinList) {
                    lexemeList.add(new Lexeme(getOffset(), pinyinItem.length(), pinyinItem.length(), 1, CharacterUtil.CHAR_ENGLISH, pinyinItem));
                    incrementOffset(pinyinItem.length());
                }
            }
        } else {
            int charType = CharacterUtil.identifyCharType(CharacterUtil.regularize(ch));
            lexemeList.add(new Lexeme(getOffset(), token.length(), token.length(), 1, charType, token));
            incrementOffset(token.length());
        }

        return lexemeList;
    }

    private List<String> getLetters(String pinyinToken) {
        int len = pinyinToken.length();
        List<String> letters = new LinkedList<String>();
        for (int idx = 0; idx < len; idx++) {
            letters.add(String.valueOf(pinyinToken.charAt(idx)));
        }
        return letters;
    }
}
