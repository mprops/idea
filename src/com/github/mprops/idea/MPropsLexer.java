package com.github.mprops.idea;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class MPropsLexer implements FlexLexer {

    enum State {
        HeaderComment,
        EolAfterHeaderComment,
        KeyMarker,
        WsBeforeKey,
        Key,
        WsAfterKey,
        EolAfterKey,
        Value,
        EolAfterValue
    }

    protected static final char KEY_MARKER_CHAR = '~';

    protected State state = State.HeaderComment;

    private int tokenStart = 0;
    private int tokenLen = 0;
    private int bufferLen = 0;
    private CharSequence buffer;

    @Override
    public void yybegin(int state) {
        this.state = State.values()[state];
    }

    @Override
    public int yystate() {
        return state.ordinal();
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
        if (buffer == null || isEOF(tokenStart + tokenLen)) {
            return null;
        }
        switch (state) {
            case HeaderComment:
                return onHeaderComment();
            case EolAfterHeaderComment:
                return onEolAfterHeaderComment();
            case KeyMarker:
                return onKeyMarker();
            case WsBeforeKey:
                return onWsBeforeKey();
            case Key:
                return onKey();
            case WsAfterKey:
                return onWsAfterKey();
            case EolAfterKey:
                return onEolAfterKey();
            case Value:
                return onValue();
            case EolAfterValue:
                return onEolAfterValue();
        }
        return MPropsElements.BAD_CHARACTER;
    }

    @NotNull
    private IElementType onHeaderComment() {
        check(tokenStart == 0, "Start of file");
        if (isKeyMarker(tokenStart)) {
            return onKeyMarker();
        }
        // read header comment
        for (; !isEOF(tokenLen); tokenLen++) {
            if (isNewLineCharFollowedByKeyMarker(tokenLen)) {
                break;
            }
        }
        if (tokenLen == 0) {
            return onEolAfterHeaderComment();
        }
        state = State.EolAfterHeaderComment;
        return MPropsElements.HEADER_COMMENT;
    }

    @NotNull
    private IElementType onEolAfterHeaderComment() {
        advanceToken();
        check(isEOL(tokenStart), "EOL");
        tokenLen = 1;
        check(isEOF(tokenStart + tokenLen) || isKeyMarker(tokenStart + tokenLen), "EOL or key marker");
        state = State.KeyMarker;
        return MPropsElements.LINE_TERMINATOR;
    }

    @NotNull
    private IElementType onKeyMarker() {
        advanceToken();
        check(isKeyMarker(tokenStart), "Key marker");
        tokenLen = 1;
        state = isWS(tokenStart + tokenLen) ? State.WsBeforeKey : State.Key;
        return MPropsElements.KEY_MARKER;
    }

    @NotNull
    private IElementType onWsBeforeKey() {
        advanceToken();
        check(isWS(tokenStart), "WS");
        skipWS();
        check(tokenLen > 0, "tokenLen > 0");
        state = State.Key;
        return MPropsElements.WHITE_SPACE;
    }

    @NotNull
    private IElementType onKey() {
        advanceToken();
        check(!isWS(tokenStart), "!WS");

        int keyEndIdx = tokenStart;
        while (!isEOL(keyEndIdx) && !isEOF(keyEndIdx)) {
            keyEndIdx++;
        }
        if (isEOF(keyEndIdx)) {
            while (keyEndIdx >= tokenStart && isWS(keyEndIdx)) {
                keyEndIdx--;
            }
        }
        tokenLen = keyEndIdx - tokenStart;
        state = isWS(tokenStart + tokenLen) ? State.WsAfterKey : State.EolAfterKey;
        return MPropsElements.KEY;
    }

    @NotNull
    private IElementType onWsAfterKey() {
        advanceToken();
        check(isWS(tokenStart), "WS");

        skipWS();
        check(tokenLen > 0, "tokenLen > 0");
        state = State.EolAfterKey;
        return MPropsElements.WHITE_SPACE;
    }

    @NotNull
    private IElementType onEolAfterKey() {
        advanceToken();
        check(isEOL(tokenStart), "EOL");

        tokenLen = 1;
        state = isKeyMarker(tokenStart) ? State.KeyMarker : State.Value;
        return MPropsElements.LINE_TERMINATOR;
    }

    @NotNull
    private IElementType onValue() {
        advanceToken();
        check(!isKeyMarker(tokenStart), "Not key marker");

        // read the value. Stop on the last line break.
        for (; !isEOF(tokenStart + tokenLen); tokenLen++) {
            if (isNewLineCharFollowedByKeyMarker(tokenStart + tokenLen)) { // the last line terminator is not included into the value
                break;
            }
        }
        if (tokenLen == 0) {
            return onEolAfterValue();
        }
        state = State.EolAfterValue;
        return MPropsElements.VALUE;
    }

    @NotNull
    private IElementType onEolAfterValue() {
        advanceToken();
        check(isEOL(tokenStart), "EOL");

        tokenLen = 1;
        check(isEOF(tokenStart + tokenLen) || isKeyMarker(tokenStart + tokenLen), "EOF or key marker");
        state = State.KeyMarker;
        return MPropsElements.LINE_TERMINATOR;
    }

    private void advanceToken() {
        check(tokenLen > 0 || tokenStart == 0 || state == State.Value, "Beginning of file or tokenLen > 0 or empty value");
        tokenStart += tokenLen;
        tokenLen = 0;
    }

    private void skipWS() {
        while (isWS(tokenStart + tokenLen) && !isEOF(tokenStart + tokenLen)) {
            tokenLen++;
        }
    }

    private boolean isNewLineCharFollowedByKeyMarker(int idx) {
        // todo: handle multi-char line breaks correctly
        return isEOL(idx) && isKeyMarker(idx + 1);
    }

    private boolean isEOF(int idx) {
        return idx >= bufferLen;
    }

    private boolean isEOL(int idx) {
        if (isEOF(idx)) {
            return false;
        }
        char c = buffer.charAt(idx);
        return c == '\n' || c == '\r';
    }

    /**
     * True if character is whitespace. Excludes line terminators.
     */
    private boolean isWS(int idx) {
        if (isEOF(idx)) {
            return false;
        }
        char c = buffer.charAt(idx);
        return c == ' ' || c == '\t';
    }

    private boolean isKeyMarker(int idx) {
        return !isEOF(idx) && buffer.charAt(idx) == KEY_MARKER_CHAR;
    }

    private void check(boolean condition, @NotNull String expectingMessage) {
        if (!condition) {
            throw new IllegalStateException("Unexpected state! Expecting " + expectingMessage);
        }
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
