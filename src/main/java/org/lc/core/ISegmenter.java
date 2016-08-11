package org.lc.core;

import java.io.IOException;

public interface ISegmenter extends IResetable {
    Lexeme next() throws IOException;
}
