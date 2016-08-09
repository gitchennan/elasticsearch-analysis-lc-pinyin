package org.lc.core;


import org.elasticsearch.common.lang3.StringUtils;
import org.lc.utils.Logger;
import org.lc.utils.MemoryUsage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class PinyinDic {

    public static final String dicLocation = "/pinyin.dic";

    public Set<String> dicSet = new HashSet<String>();

    private static PinyinDic instance;

    private PinyinDic() {
        initialize();
    }

    private void initialize() {
        InputStream in = PinyinDic.class.getResourceAsStream(dicLocation);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line = null;
            long startPoint = System.currentTimeMillis();
            while (null != (line = reader.readLine())) {
                if (StringUtils.isNotBlank(line)) {
                    dicSet.add(line);
                }
            }
            long endPoint = System.currentTimeMillis();
            Logger.logger.info(String.format("Load pinyin from pinyin.dic, sizeof dic=[%s], takes %s ms, size=%s",
                    MemoryUsage.humanSizeOf(dicSet), (endPoint - startPoint), dicSet.size()), this);
        } catch (Exception ex) {
            Logger.logger.error("read pinyin dic error.", ex);
            throw new RuntimeException("read pinyin dic error.", ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ex) {
                //ignore ex
            }
        }
    }

    public static PinyinDic getInstance() {
        if (instance == null) {
            synchronized (PinyinDic.class) {
                if (instance == null) {
                    instance = new PinyinDic();
                }
            }
        }
        return instance;
    }

    public boolean contains(String c) {
        return dicSet.contains(c);
    }
}
