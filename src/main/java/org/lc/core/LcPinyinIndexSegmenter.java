package org.lc.core;

import org.lc.utils.CharacterUtil;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class LcPinyinIndexSegmenter extends AbstractPinyinSegmenter {

    public LcPinyinIndexSegmenter(Reader input) {
        super(input);
    }

    protected List<Lexeme> processTokenToLexeme(String token) {
        List<Lexeme> lexemeList = new LinkedList<Lexeme>();
        char ch = token.charAt(0);
        if (token.length() == 1 && CharacterUtil.isChinese(ch)) {
            String[] pinyinArray = convertChineseToPinyin(ch, true);

            lexemeList.add(new Lexeme(getOffset(), 1, 1, 1, CharacterUtil.CHAR_CHINESE, String.valueOf(ch)));
            for (int idx = 0; idx < pinyinArray.length; idx++) {
                Lexeme tmpLexeme = new Lexeme(getOffset(), pinyinArray[idx].length(), 1, 0, CharacterUtil.CHAR_CHINESE, pinyinArray[idx]);
                lexemeList.add(tmpLexeme);
            }

            incrementOffset(1);
        } else {
            int charType = CharacterUtil.identifyCharType(CharacterUtil.regularize(ch));
            lexemeList.add(new Lexeme(getOffset(), token.length(), token.length(), 1, charType, token));
            incrementOffset(token.length());
        }
        return lexemeList;
    }
}
