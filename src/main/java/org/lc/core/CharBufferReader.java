package org.lc.core;


import org.apache.lucene.analysis.CharacterUtils;

import java.io.IOException;
import java.io.Reader;

public class CharBufferReader implements IResetable {
    //字符窜reader
    private Reader input;
    //读取游标
    private int readCursor = 0;
    //字符读入缓冲
    private CharacterUtils.CharacterBuffer charBuffer;
    //字符缓冲区大小
    private int bufferSize;

    public CharBufferReader(Reader input, int bufferSize) {
        this.input = input;
        this.bufferSize = bufferSize;
        charBuffer = CharacterUtils.newCharacterBuffer(bufferSize);
    }

    public Character seek() throws IOException {
        if (readCursor < charBuffer.getLength()) {
            return charBuffer.getBuffer()[readCursor];
        }
        if (readToBuffer()) {
            return seek();
        }
        return null;
    }

    public Character read() throws IOException {
        if (readCursor < charBuffer.getLength()) {
            ++readCursor;
            return charBuffer.getBuffer()[readCursor - 1];
        }
        if (readToBuffer()) {
            return read();
        }
        return null;
    }


    private boolean readToBuffer() throws IOException {
        CharacterUtils.fill(charBuffer, input);
        readCursor = charBuffer.getOffset();
        return charBuffer.getLength() > charBuffer.getOffset();
    }

    public void reset(Reader input) {
        this.input = input;
        readCursor = 0;
        charBuffer = CharacterUtils.newCharacterBuffer(bufferSize);
    }

}
