package com.github.mulpr;

import com.github.mulpr.psi.MulprElements;
import com.github.mulpr.psi.MulprLexerElements;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

public class MulprLexer implements FlexLexer {

    private static final int STATE_PARSING_MARKER = 0;
    private static final int STATE_PARSING_KEY = 1;
    private static final int STATE_PARSING_VALUE = 2;

    private int state = 0;

    private int tokenStart = 0;
    private int tokenLen = 0;
    private int bufferLen = 0;
    private CharSequence buffer;

    @Override
    public void yybegin(int state) {
        this.state = state;
    }

    @Override
    public int yystate() {
        return state;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return tokenStart + tokenLen;
    }

    /**
     * Returns null on EOF.
     */
    @Override
    public IElementType advance() {
        if (buffer == null || tokenStart + tokenLen == buffer.length()) {
            return null;
        }
        switch (state) {
            case STATE_PARSING_MARKER:
                tokenStart += tokenLen;
                tokenLen = 1;
                if (buffer.charAt(tokenStart) != '~') {
                    return MulprLexerElements.BAD_CHARACTER;
                }
                state = STATE_PARSING_KEY;
                return MulprLexerElements.KEY_MARKER;
            case STATE_PARSING_KEY: {
                tokenStart += tokenLen;
                tokenLen = 0;
                for (int i = tokenStart; i < bufferLen; i++) {
                    tokenLen++;
                    if (buffer.charAt(i) == '\n') { // todo: handle all line breaks correctly
                        break;
                    }
                }
                state = STATE_PARSING_VALUE;
                return MulprElements.KEY;
            }
            case STATE_PARSING_VALUE:
                //todo: check if not new key start (if prev was empty)
                tokenStart += tokenLen;
                tokenLen = 0;
                for (int i = tokenStart; i < bufferLen; i++) {
                    tokenLen++;
                    if (buffer.charAt(i) == '\n' && i + 1 < bufferLen - 1 && buffer.charAt(i + 1) == '~') { // todo: handle all line breaks correctly
                        break;
                    }
                }
                state = STATE_PARSING_MARKER;
                return MulprElements.VALUE;
        }
        return MulprLexerElements.BAD_CHARACTER;
    }

    @Override
    public void reset(CharSequence buffer, int start, int end, int initialState) {
        this.buffer = buffer;
        this.tokenStart = start;
        this.tokenLen = 0;
        this.bufferLen = end;
        yybegin(initialState);
    }
}
