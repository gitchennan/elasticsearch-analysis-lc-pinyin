package org.lc.core;

public class Lexeme {
    //词元的起始位移
    private int offset;
    //词元的长度
    private int length;
    //词条原始长度
    private int oriLength;
    //词元文本
    private String lexemeText;
    //词元类型
    private int lexemeType;
    //当前次元距上一个距离(default=1)
    private int incrementStep = 1;

    public Lexeme(int offset, int length, int oriLength, int incrementStep, int lexemeType, String lexemeText) {
        this.offset = offset;
        if (length < 0 || oriLength < 0) {
            throw new IllegalArgumentException("length|oriLength < 0");
        }
        this.oriLength = oriLength;
        this.length = length;
        this.lexemeType = lexemeType;
        this.lexemeText = lexemeText;
        this.incrementStep = incrementStep;
    }

    public int getIncrementStep() {
        return incrementStep;
    }

    public void setIncrementStep(int incrementStep) {
        this.incrementStep = incrementStep;
    }

    public int beginPosition() {
        return offset;
    }

    public int endPosition() {
        return offset + oriLength;
    }

    public int getOriLength() {
        return oriLength;
    }

    public void setOriLength(int oriLength) {
        this.oriLength = oriLength;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        if (this.length < 0) {
            throw new IllegalArgumentException("length|oriLength < 0");
        }
        this.length = length;
    }

    public String getLexemeText() {
        if (lexemeText == null) {
            return "";
        }
        return lexemeText;
    }

    public void setLexemeText(String lexemeText) {
        if (lexemeText == null) {
            this.lexemeText = "";
            this.length = 0;
        } else {
            this.lexemeText = lexemeText;
            this.length = lexemeText.length();
        }
    }

    public int getLexemeType() {
        return lexemeType;
    }

    public void setLexemeType(int lexemeType) {
        this.lexemeType = lexemeType;
    }
}
