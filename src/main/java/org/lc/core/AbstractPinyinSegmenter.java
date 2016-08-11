package org.lc.core;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.lc.utils.CharacterUtil;
import org.lc.utils.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPinyinSegmenter implements ISegmenter {
    //当前处理偏移位(相对于全文)
    private int offset = 0;
    //上一次处理的字符
    private Character previousChar;
    //上一次处理的字符类型
    private int previousCharType = CharacterUtil.CHAR_USELESS;
    //字符串reader
    private final Reader input;
    //字符缓存
    private final CharBufferReader charBufferReader;
    //字符缓冲区大小
    protected final static int CHAR_BUFFER_SIZE = 1024;
    //拼音格式
    protected final HanyuPinyinOutputFormat format;
    //词元缓存
    private final LinkedList<Lexeme> lexemeCache = new LinkedList<Lexeme>();

    protected abstract List<Lexeme> processTokenToLexeme(String token);

    public AbstractPinyinSegmenter(Reader input) {
        this.input = input;
        charBufferReader = new CharBufferReader(input, CHAR_BUFFER_SIZE);
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        offset = 0;
    }

    @Override
    public final void reset() {
        offset = 0;
        charBufferReader.reset();
        lexemeCache.clear();
        previousChar = null;
        previousCharType = CharacterUtil.CHAR_USELESS;
    }

    protected final Character readChar() throws IOException {
        return charBufferReader.read();
    }

    protected final Character seekChar() throws IOException {
        return charBufferReader.seek();
    }

    /**
     * 分词，获取下一个词元
     *
     * @return Lexeme 词元对象
     * @throws java.io.IOException
     */
    public final Lexeme next() throws IOException {
        if (hasMoreLexemeInCache()) {
            return nextCacheLexeme();
        }
        StringBuilder lexemeBuilder = new StringBuilder();
        Character curChar = readChar();
        if (curChar == null) {
            return null;
        }
        lexemeBuilder.append(curChar);
        previousChar = curChar;
        previousCharType = CharacterUtil.identifyCharType(CharacterUtil.regularize(curChar));

        while (true) {
            curChar = seekChar();
            if (curChar == null || breakProcess(previousChar, previousCharType, curChar)) {
                break;
            }
            lexemeBuilder.append(readChar());
        }
        if (lexemeBuilder.length() > 0) {
            List<Lexeme> lexemeList = processTokenToLexeme(lexemeBuilder.toString());
            if (lexemeList != null && lexemeList.size() > 0) {
                Iterator<Lexeme> it = lexemeList.iterator();
                Lexeme retLexeme = it.next();
                while (it.hasNext()) {
                    addLexemeToCache(it.next());
                }
                return retLexeme;
            }
        }
        return null;
    }

    protected String[] convertChineseToPinyin(char ch, boolean withFirstLetter) {
        String[] pinyinArray = null;
        try {
            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            if (pinyinArray == null) {
                pinyinArray = new String[]{String.valueOf(ch)};
            }
            LinkedHashSet<String> pinyinSet = new LinkedHashSet<String>();
            for (int idx = 0; idx < pinyinArray.length; idx++) {
                pinyinSet.add(pinyinArray[idx]);
                if (withFirstLetter) {
                    pinyinSet.add(String.valueOf(pinyinArray[idx].charAt(0)));
                }
            }
            pinyinArray = pinyinSet.toArray(new String[pinyinSet.size()]);
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            Logger.logger.error(ex.getMessage(), ex);
            pinyinArray = new String[]{String.valueOf(ch)};
        }
        return pinyinArray;
    }

    private boolean breakProcess(Character previousChar, int previousCharType, Character curChar) {
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

    protected boolean hasMoreLexemeInCache() {
        return !lexemeCache.isEmpty();
    }

    private Lexeme nextCacheLexeme() {
        return lexemeCache.pollFirst();
    }

    private void addLexemeToCache(Lexeme lexeme) {
        if (lexeme != null) {
            lexemeCache.add(lexeme);
        }
    }

    protected final void incrementOffset(int increment) {
        offset += increment;
    }

    public int getOffset() {
        return offset;
    }
}
