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
                if (isEOL(tokenStart)) {
                    tokenLen = 1;
                    return MPropsElements.LINE_TERMINATOR;
                }
                if (!isKeyMarker(tokenStart)) {
                    if (tokenStart != 0) { // if not a header and not a key marker -> error
                        return MPropsElements.BAD_CHARACTER;
                    }
                    for (int i = tokenStart; i < bufferLen; i++) { // read header comment
                        if (isNewLineCharFollowedByKeyMarker(i)) {
                            break;
                        }
                        tokenLen++;
                    }
                    return MPropsElements.HEADER_COMMENT;
                }
                tokenLen = 1;
                state = STATE_PARSING_KEY;
                return MPropsElements.KEY_MARKER;
            case STATE_PARSING_KEY: {
                advanceToken();
                for (int i = tokenStart; i < bufferLen; i++) { //todo: heading & trailing spaces?
                    tokenLen++;
                    if (isEOL(i)) {
                        break;
                    }
                }
                state = STATE_PARSING_VALUE;
                return MPropsElements.KEY;
            }
            case STATE_PARSING_VALUE:
                advanceToken();
                if (buffer.charAt(tokenStart) == KEY_MARKER_CHAR) { // value is empty -> new keys follows the previous one.
                    state = STATE_PARSING_MARKER;
                    return MPropsElements.VALUE;
                }
                // read the value. Stop on the last line break.
                for (int i = tokenStart; i < bufferLen; i++) {
                    if (isNewLineCharFollowedByKeyMarker(i)) { // the last line terminator is not included into the value
                        break;
                    }
                    tokenLen++;
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

    private boolean isNewLineCharFollowedByKeyMarker(int idx) {
        // todo: handle all line breaks correctly
        return isEOL(idx) && isKeyMarker(idx + 1);
    }

    private boolean isEOL(int idx) {
        if (idx > bufferLen) {
            return false;
        }
        char c = buffer.charAt(idx);
        return c == '\n' || c == '\r';
    }

    private boolean isKeyMarker(int idx) {
        return idx <= bufferLen - 1 && buffer.charAt(idx) == KEY_MARKER_CHAR;
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
