package org.lc.utils;


import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.LinkedHashSet;

public class PinyinHelper {

    //拼音格式
    private static final HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static {
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    private PinyinHelper() {

    }

    public static String[] convertChineseToPinyin(char ch, boolean withFirstLetter) {
        String[] pinyinArray = null;
        try {
            pinyinArray = net.sourceforge.pinyin4j.PinyinHelper.toHanyuPinyinStringArray(ch, format);
            if (pinyinArray == null) {
                return new String[0];
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
}
