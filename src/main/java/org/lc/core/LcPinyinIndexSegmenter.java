package org.lc.core;

import org.lc.utils.CharacterUtil;
import org.lc.utils.PinyinHelper;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LcPinyinIndexSegmenter extends AbstractPinyinSegmenter {

    private int analysisSetting;

    public LcPinyinIndexSegmenter(Reader input, int analysisSetting) {
        super(input);
        this.analysisSetting = analysisSetting;
    }

    protected List<Lexeme> processTokenToLexeme(String token) {
        List<Lexeme> lexemeList = new LinkedList<Lexeme>();
        char ch = token.charAt(0);
        if (token.length() == 1 && CharacterUtil.isChinese(ch)) {
            String[] pinyinArray = convertChineseToPinyin(ch);
            for (int idx = 0; idx < pinyinArray.length; idx++) {
                Lexeme tmpLexeme = null;
                if (idx == 0) {
                    tmpLexeme = new Lexeme(getOffset(), pinyinArray[idx].length(), 1, 1, CharacterUtil.CHAR_CHINESE, pinyinArray[idx]);
                } else {
                    tmpLexeme = new Lexeme(getOffset(), pinyinArray[idx].length(), 1, 0, CharacterUtil.CHAR_CHINESE, pinyinArray[idx]);
                }
                lexemeList.add(tmpLexeme);
            }

            incrementOffset(1);
        } else {
            int charType = CharacterUtil.identifyCharType(CharacterUtil.regularize(ch));
            lexemeList.add(new Lexeme(getOffset(), token.length(), token.length(), 1, charType, token));
            if (token.length() > 1 && (CharacterUtil.CHAR_ARABIC == charType || CharacterUtil.CHAR_ENGLISH == charType)) {
                for (int idx = 0; idx < token.length(); idx++) {
                    Lexeme tmpLexeme = new Lexeme(getOffset() + idx, 1, 1, 1, charType, token.charAt(idx) + "");
                    lexemeList.add(tmpLexeme);
                }
            }
            incrementOffset(token.length());
        }
        return lexemeList;
    }


    private String[] convertChineseToPinyin(char ch) {
        List<String> tokens = new ArrayList<String>();
        if ((analysisSetting & AnalysisSetting.IndexAnalysisSetting.chinese_char) == AnalysisSetting.IndexAnalysisSetting.chinese_char) {
            tokens.add(String.valueOf(ch));
        }
        if ((analysisSetting & AnalysisSetting.IndexAnalysisSetting.full_pinyin) == AnalysisSetting.IndexAnalysisSetting.full_pinyin ||
                (analysisSetting & AnalysisSetting.IndexAnalysisSetting.first_letter) == AnalysisSetting.IndexAnalysisSetting.first_letter) {
            String[] fullPinyin = PinyinHelper.convertChineseToPinyin(ch, false);
            for (String pinyinItem : fullPinyin) {
                if ((analysisSetting & AnalysisSetting.IndexAnalysisSetting.full_pinyin) == AnalysisSetting.IndexAnalysisSetting.full_pinyin) {
                    tokens.add(pinyinItem);
                }
                if ((analysisSetting & AnalysisSetting.IndexAnalysisSetting.first_letter) == AnalysisSetting.IndexAnalysisSetting.first_letter) {
                    tokens.add(pinyinItem.substring(0, 1));
                }
            }
        }
        return tokens.toArray(new String[tokens.size()]);
    }
}
