package org.lc.core;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.lc.utils.CharacterUtil;
import org.lc.utils.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashSet;

public class LcPinyinSearchSegmenter implements ISegmenter {
    //当前处理偏移位(相对于全文)
    private int offset = 0;
    //字符串reader
    private Reader input;
    //字符缓存
    private CharBufferReader charBufferReader;
    //拼音格式
    private final HanyuPinyinOutputFormat format;
    //字符缓冲区大小
    private static final int CHAR_BUFFER_SIZE = 1024;

    public LcPinyinSearchSegmenter(Reader input) {
        this.input = input;
        charBufferReader = new CharBufferReader(input, CHAR_BUFFER_SIZE);
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public Lexeme next() throws IOException {
        Character ch = charBufferReader.read();
        if (ch == null) {
            return null;
        }
        String pinyin = convertChineseToPinyin(ch);
        Lexeme lexeme = new Lexeme(offset, pinyin.length(), 1, 1, CharacterUtil.CHAR_CHINESE, pinyin);
        ++offset;
        return lexeme;
    }

    public String convertChineseToPinyin(char ch) {
        String[] pinyinArray = new String[0];
        try {
            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            LinkedHashSet<String> pinyinSet = new LinkedHashSet<String>();
            for (int idx = 0; idx < pinyinArray.length; idx++) {

                pinyinSet.add(pinyinArray[idx]);
            }
            pinyinArray = pinyinSet.toArray(new String[pinyinSet.size()]);
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            Logger.logger.error(ex.getMessage(), ex);
        }
        return pinyinArray.length > 0 ? pinyinArray[0] : "";
    }

    @Override
    public void reset() {
        charBufferReader.reset();
    }
}
