package org.lc.core;

import org.apache.lucene.analysis.util.CharacterUtils;

import java.io.IOException;
import java.io.Reader;

public class CharBufferReader implements IResetable {
    //字符窜reader
    private Reader input;
    //读取游标
    private int readCursor = 0;
    //字符读入缓冲
    private CharacterUtils.CharacterBuffer charBuffer;

    public CharBufferReader(Reader input, int bufferSize) {
        this.input = input;
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
        CharacterUtils.getInstance().fill(charBuffer, input);
        readCursor = charBuffer.getOffset();
        return charBuffer.getLength() > charBuffer.getOffset();
    }

    public void reset() {
        readCursor = 0;
    }

}
