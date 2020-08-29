package org.lc.core;

import org.elasticsearch.common.settings.Settings;

import java.util.Arrays;
import java.util.List;

public class AnalysisSetting {
    public static final String analysisMode = "mode";
    public static final String index = "index";
    public static final String search = "search";


    public static class IndexAnalysisSetting {
        /*全拼*/
        public static final int full_pinyin = 1;
        /*首字母*/
        public static final int first_letter = 2;
        /*中文单字符*/
        public static final int chinese_char = 4;
    }

    public static class SearchAnalysisSetting {
        /*最优拼音拆分*/
        public static final int smart_pinyin = 1;
        /*单字母拆分(用于首字母匹配)*/
        public static final int single_letter = 2;
    }

    public static int parseIndexAnalysisSettings(Settings settings) {
        int settingCode = 0;
        if(settings != null) {
            String[] defaultSetting = new String[]{"chinese_char", "first_letter", "full_pinyin"};
            List<String> indexAnalysisSetting = settings.getAsList(analysisMode, Arrays.asList(defaultSetting));
            for (String settingItem : indexAnalysisSetting) {
                if ("chinese_char".equalsIgnoreCase(settingItem)) {
                    settingCode |= IndexAnalysisSetting.chinese_char;
                }
                if ("first_letter".equalsIgnoreCase(settingItem)) {
                    settingCode |= IndexAnalysisSetting.first_letter;
                }
                if ("full_pinyin".equalsIgnoreCase(settingItem)) {
                    settingCode |= IndexAnalysisSetting.full_pinyin;
                }
            }
        }
        if (settingCode == 0) {
            settingCode = IndexAnalysisSetting.chinese_char | IndexAnalysisSetting.first_letter | IndexAnalysisSetting.full_pinyin;
        }
        return settingCode;
    }

    public static int parseSearchAnalysisSettings(Settings settings) {
        if(settings != null) {
            String setting = settings.get(analysisMode, "smart_pinyin");
            if ("single_letter".equalsIgnoreCase(setting)) {
                return SearchAnalysisSetting.single_letter;
            }
        }
        return SearchAnalysisSetting.smart_pinyin;
    }

}
