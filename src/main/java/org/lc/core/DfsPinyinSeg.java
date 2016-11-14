package org.lc.core;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DfsPinyinSeg {
    //正向匹配最大词条长度
    private static final int MAX_TOKEN_LENGTH = 6;
    //记忆化搜索存储
    private PinyinNode[][] dfsMemory;
    //待分词拼音文本
    private String text;

    public DfsPinyinSeg(String text) {
        this.text = text;
        int dfsMapSize = text.length();
        dfsMemory = new PinyinNode[dfsMapSize][dfsMapSize];
    }


    public List<String> segDeepSearch() {
        if (text == null || text.trim().length() == 0) {
            return Collections.emptyList();
        }
        int low = 0;
        int high = text.length() - 1;
        PinyinNode pinyinNode = dfsTokenize(text.toCharArray(), low, high);
        return pinyinNode.getPinyinTokens();
    }

    /**
     * 记忆化深度搜索实现,这种方法比反向最大匹配效率高2-3倍
     *
     * @param text 待分词拼音串
     * @param low  起始分词位置(从0开始)
     * @param high 结束分词位置(从0开始,包含)
     * @return     分词后词条
     */
    private PinyinNode dfsTokenize(char[] text, int low, int high) {
        if (dfsMemory[low][high] != null) {
            return dfsMemory[low][high];
        }
        if (low == high) {
            PinyinNode tNode = new PinyinNode(newStringFromChars(text, low, high));
            dfsMemory[low][high] = tNode;
            return tNode;
        }
        int minSelectedIndex = Integer.MAX_VALUE;
        int minPinyinTokenCount = Integer.MAX_VALUE;
        int idx = low + MAX_TOKEN_LENGTH - 1;
        idx = idx > high ? high : idx;

        for (; idx > low; idx--) {
            String pyToken = newStringFromChars(text, low, idx);
            if (PinyinDic.getInstance().contains(pyToken)) {
                if (idx + 1 <= high) {
                    PinyinNode nextPinyinNode = dfsTokenize(text, idx + 1, high);
                    dfsMemory[idx + 1][high] = nextPinyinNode;
                    if (nextPinyinNode.tokenCount() <= minPinyinTokenCount) {
                        minPinyinTokenCount = nextPinyinNode.tokenCount();
                        minSelectedIndex = idx;
                    }
                } else {
                    PinyinNode curPinyinNode = new PinyinNode(pyToken);
                    dfsMemory[low][high] = curPinyinNode;
                    minSelectedIndex = idx;
                }
            }
        }
        if (dfsMemory[low][high] != null) {
            return dfsMemory[low][high];
        }
        PinyinNode firstCharPinyinNode = dfsTokenize(text, low + 1, high);
        if (firstCharPinyinNode.tokenCount() <= minPinyinTokenCount) {
            minSelectedIndex = low;
        }
        String curText = newStringFromChars(text, low, minSelectedIndex);
        dfsMemory[low][high] = new PinyinNode(curText, dfsMemory[minSelectedIndex + 1][high]);
        return dfsMemory[low][high];
    }

    private String newStringFromChars(char[] text, int low, int higt) {
        return new String(text, low, higt - low + 1);
    }

    public void cleanDfsMap() {
        int dfsMapSize = text.length();
        dfsMemory = new PinyinNode[dfsMapSize][dfsMapSize];
    }

    class PinyinNode {
        //词条数
        private int tokenCount = 0;
        //词条
        private List<String> pinyinTokens;

        public PinyinNode(String pinyin) {
            this(pinyin, null);
        }

        public PinyinNode(String pinyin, PinyinNode pyNode) {
            pinyinTokens = new LinkedList<String>();
            pinyinTokens.add(pinyin);
            if (pyNode != null) {
                pinyinTokens.addAll(pyNode.getPinyinTokens());
            }
            tokenCount = pinyinTokens.size();
        }

        public List<String> getPinyinTokens() {
            return pinyinTokens;
        }

        public int tokenCount() {
            return this.tokenCount;
        }
    }
}
