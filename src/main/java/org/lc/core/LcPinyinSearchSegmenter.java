package org.lc.core;


import org.lc.utils.CharacterUtil;

import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class LcPinyinSearchSegmenter extends AbstractPinyinSegmenter {

    public LcPinyinSearchSegmenter(Reader input) {
        super(input);
    }

    @Override
    protected List<Lexeme> processTokenToLexeme(String token) {
        List<Lexeme> lexemeList = new LinkedList<Lexeme>();
        char ch = token.charAt(0);
        if (CharacterUtil.isLetter(ch)) {
            List<String> pinyinList = smartPinyinSeg(token);
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
     * 反向最大匹配算法实现分词(不建议使用)
     *
     * @param text      待分词拼音串
     * @param maxLength 最大词条长度
     * @return          分词后词条
     */
    @Deprecated
    public List<String> segReverse(String text, int maxLength) {
        text = text.toLowerCase(Locale.ENGLISH);
        List<String> result = new LinkedList<String>();
        PinyinDic dic = PinyinDic.getInstance();
        while (text.length() > 0) {
            if (text.length() < maxLength) {
                maxLength = text.length();
            }
            String tryWord = text.substring(text.length() - maxLength);
            while (!dic.contains(tryWord)) {
                if (tryWord.length() == 1) {
                    break;
                }
                tryWord = tryWord.substring(1);
            }
            result.add(tryWord);
            text = text.substring(0, text.length() - tryWord.length());
        }
        Collections.reverse(result);
        return result;
    }
}
