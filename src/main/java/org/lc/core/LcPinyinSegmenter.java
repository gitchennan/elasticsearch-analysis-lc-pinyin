package org.lc.core;

import org.lc.utils.CharacterUtil;
import org.lc.utils.Logger;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;

public class LcPinyinSegmenter implements Resetable {
    //当前处理偏移位(相对于全文)
    private int offset = 0;
    //字符串reader
    private Reader input;
    //上一次处理的字符
    private Character previousChar;
    //上一次处理的字符类型
    private int previousCharType = CharacterUtil.CHAR_USELESS;
    //字符缓存
    private CharBufferReader charBufferReader;
    //多音字拼音
    private final LinkedList<Lexeme> pinyinList = new LinkedList<Lexeme>();
    //拼音格式
    private final HanyuPinyinOutputFormat format;
    //字符缓冲区大小
    private static final int CHAR_BUFFER_SIZE = 1024;
    private String analysisMode;

    public LcPinyinSegmenter(String analysisMode, Reader input) {
        this.input = input;
        charBufferReader = new CharBufferReader(input, CHAR_BUFFER_SIZE);
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        this.analysisMode = analysisMode;
    }

    public void reset() {
        offset = 0;
        previousChar = null;
        previousCharType = CharacterUtil.CHAR_USELESS;
        pinyinList.clear();
        charBufferReader.reset();
    }


    /**
     * 分词，获取下一个词元
     *
     * @return Lexeme 词元对象
     * @throws java.io.IOException
     */
    public Lexeme next() throws IOException {
        if (needContinueProcessMultiPinyin()) {
            return pinyinList.pollFirst();
        }
        StringBuilder lexemeBuilder = new StringBuilder();
        Character curChar = charBufferReader.read();
        if (curChar == null) {
            return null;
        }
        lexemeBuilder.append(curChar);
        previousChar = curChar;
        previousCharType = CharacterUtil.identifyCharType(CharacterUtil.regularize(curChar));

        while (true) {
            curChar = charBufferReader.seek();
            if (curChar == null || breakProcess(previousChar, curChar)) {
                break;
            }
            lexemeBuilder.append(charBufferReader.read());
        }
        if (lexemeBuilder.length() > 0) {
            return processTokenToLexeme(lexemeBuilder.toString(), analysisMode);
        }
        return null;
    }

    public String[] convertChineseToPinyin(char ch, String analysisMode) {
        String[] pinyinArray = new String[0];
        try {
            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            HashSet<String> pinyinSet = new HashSet<String>();
            for (int idx = 0; idx < pinyinArray.length; idx++) {
                if (AnalysisSetting.first_letter_only.equals(analysisMode)) {
                    pinyinSet.add(String.valueOf(pinyinArray[idx].charAt(0)));
                } else {
                    pinyinSet.add(pinyinArray[idx]);
                }
            }
            return pinyinSet.toArray(new String[pinyinSet.size()]);
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            Logger.logger.error(ex.getMessage(), ex);
        }
        return pinyinArray;
    }

    private Lexeme processTokenToLexeme(String token, String analysisMode) {
        Lexeme lexeme = null;
        char ch = token.charAt(0);
        if (token.length() == 1 && CharacterUtil.isChinese(ch)) {
            String[] pinyinArray = convertChineseToPinyin(ch, analysisMode);
            if (pinyinArray.length <= 0) {
                ++offset;
                return null;
            }

            lexeme = new Lexeme(offset, pinyinArray[0].length(), 1, CharacterUtil.CHAR_CHINESE, pinyinArray[0]);
            for (int idx = 1; idx < pinyinArray.length; idx++) {
                pinyinList.add(new Lexeme(offset, pinyinArray[1].length(), 1, CharacterUtil.CHAR_CHINESE, pinyinArray[idx]));
            }

            ++offset;
        } else {
            int charType = CharacterUtil.identifyCharType(CharacterUtil.regularize(ch));
            lexeme = new Lexeme(offset, token.length(), token.length(), charType, token);
            offset = offset + token.length();
        }
        return lexeme;
    }

    private boolean breakProcess(Character previousChar, Character curChar) {
        int curCharType = CharacterUtil.identifyCharType(CharacterUtil.regularize(curChar));
        if (curCharType != previousCharType) {
            return true;
        } else {
            if (CharacterUtil.isChinese(previousChar) && CharacterUtil.isChinese(curChar)) {
                return true;
            }
        }
        return false;
    }

    private boolean needContinueProcessMultiPinyin() {
        return !pinyinList.isEmpty();
    }
}
