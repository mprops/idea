package com.github.mprops.idea;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

public class MPropsLexer implements FlexLexer {

    private static final int STATE_PARSING_MARKER = 0;
    private static final int STATE_PARSING_KEY = 1;
    private static final int STATE_PARSING_VALUE = 2;
    private static final char KEY_MARKER_CHAR = '~';

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
                advanceToken();
                if (buffer.charAt(tokenStart) != '~') {
                    if (tokenStart != 0) {
                        return MPropsElements.BAD_CHARACTER;
                    }
                    // header comment zone
                    for (int i = tokenStart; i < bufferLen; i++) {
                        tokenLen++;
                        if (isNewLineCharFollowedByKeyMarker(i)) {
                            break;
                        }
                    }
                    return MPropsElements.HEADER_COMMENT;
                }
                tokenLen = 1;
                state = STATE_PARSING_KEY;
                return MPropsElements.KEY_MARKER;
            case STATE_PARSING_KEY: {
                advanceToken();
                for (int i = tokenStart; i < bufferLen; i++) {
                    tokenLen++;
                    if (buffer.charAt(i) == '\n') { // todo: handle all line breaks correctly
                        break;
                    }
                }
                state = STATE_PARSING_VALUE;
                return MPropsElements.KEY;
            }
            case STATE_PARSING_VALUE:
                advanceToken();
                if (buffer.charAt(tokenStart) == KEY_MARKER_CHAR) {
                    state = STATE_PARSING_MARKER;
                    return MPropsElements.VALUE;
                }
                for (int i = tokenStart; i < bufferLen; i++) {
                    tokenLen++;
                    if (isNewLineCharFollowedByKeyMarker(i)) {
                        break;
                    }
                }
                state = STATE_PARSING_MARKER;
                return MPropsElements.VALUE;
        }
        return MPropsElements.BAD_CHARACTER;
    }

    private void advanceToken() {
        tokenStart += tokenLen;
        tokenLen = 0;
    }

    private boolean isNewLineCharFollowedByKeyMarker(int i) {
        // todo: handle all line breaks correctly
        return buffer.charAt(i) == '\n' && i + 1 <= bufferLen - 1 && buffer.charAt(i + 1) == KEY_MARKER_CHAR;
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
