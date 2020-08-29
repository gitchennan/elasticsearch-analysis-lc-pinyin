package org.lc.core;

import org.lc.utils.CharacterUtil;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
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
    private Reader input;
    //字符缓存
    private final CharBufferReader charBufferReader;
    //字符缓冲区大小
    protected final static int CHAR_BUFFER_SIZE = 1024;
    //词元缓存
    private final LinkedList<Lexeme> lexemeCache = new LinkedList<Lexeme>();

    protected abstract List<Lexeme> processTokenToLexeme(String token);

    public AbstractPinyinSegmenter(Reader input) {
        this.input = input;
        charBufferReader = new CharBufferReader(input, CHAR_BUFFER_SIZE);
        offset = 0;
    }

    @Override
    public final void reset(Reader input) {
        this.input = input;
        offset = 0;
        charBufferReader.reset(input);
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
     * @throws IOException
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
