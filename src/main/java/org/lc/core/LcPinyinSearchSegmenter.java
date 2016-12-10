package org.lc.core;


import org.lc.utils.CharacterUtil;

import java.io.Reader;
import java.util.*;

public class LcPinyinSearchSegmenter extends AbstractPinyinSegmenter {

    private int analysisSetting;

    public LcPinyinSearchSegmenter(Reader input, int analysisSetting) {
        super(input);
        this.analysisSetting = analysisSetting;
    }

    @Override
    protected List<Lexeme> processTokenToLexeme(String token) {
        List<Lexeme> lexemeList = new LinkedList<Lexeme>();
        char ch = token.charAt(0);
        if (CharacterUtil.isLetter(ch)) {
            List<String> pinyinList = null;
            if(analysisSetting == AnalysisSetting.SearchAnalysisSetting.smart_pinyin) {
                pinyinList = smartPinyinSeg(token);
            } else {
                pinyinList = singleChar(token);
            }

            if (pinyinList.size() > 0) {
                for (String pinyinItem : pinyinList) {
                    lexemeList.add(new Lexeme(getOffset(), pinyinItem.length(), pinyinItem.length(), 1, CharacterUtil.CHAR_ENGLISH, pinyinItem));
                    incrementOffset(pinyinItem.length());
                }
            }
        } else {
            //中文,数字,其他字符不做处理
            int charType = CharacterUtil.identifyCharType(CharacterUtil.regularize(ch));
            lexemeList.add(new Lexeme(getOffset(), token.length(), token.length(), 1, charType, token));
            incrementOffset(token.length());
        }

        return lexemeList;
    }

    /**
     * 智能拼音拆分,剩下的单字母越少越好
     * @param pinyinToken 待分词拼音串
     * @return            分词后词条
     */
    private List<String> smartPinyinSeg(String pinyinToken) {
        DfsPinyinSeg dfsPinyinSeg = new DfsPinyinSeg(pinyinToken);
        return dfsPinyinSeg.segDeepSearch();
    }

    /**
     * 单字符拆分拼音(适用于首字母搜索)
     * @param token 待分词拼音串
     * @return      分词后词条
     */
    private List<String> singleChar(String token) {
        if(token == null || token.trim().length() == 0) {
            return Collections.emptyList();
        }
        List<String> charTokenList = new ArrayList<String>(token.length());
        for (char ch : token.toCharArray()) {
            charTokenList.add(String.valueOf(ch));
        }
        return charTokenList;
    }
}
