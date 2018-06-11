package com.github.mprops.idea;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPropsLexer extends LexerBase {

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

    protected State state;
    protected IElementType tokenType;
    private int tokenStart;
    private int tokenLen;
    private CharSequence buffer;
    private int wholeBufferStart;
    private int wholeBufferEnd;
    private CharSequence wholeBuffer;

    @Override
    public void start(@NotNull CharSequence wholeBuffer, int startOffset, int endOffset, int initialState) {
        this.state = State.HeaderComment;
        this.tokenType = MPropsElements.HEADER_COMMENT;
        this.tokenStart = 0;
        this.tokenLen = 0;
        this.wholeBuffer = wholeBuffer;
        this.wholeBufferStart = startOffset;
        this.wholeBufferEnd = endOffset;
        this.buffer = wholeBuffer.subSequence(startOffset, endOffset);
    }

    @Override
    public int getState() {
        return state.ordinal();
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        return tokenType;
    }

    @Override
    public int getTokenStart() {
        return wholeBufferStart + tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return wholeBufferStart + tokenStart + tokenLen;
    }

    /**
     * Returns null on EOF.
     */
    @Override
    public void advance() {
        if (buffer == null || isEOF(tokenStart + tokenLen)) {
            if ((state == State.WsBeforeKey || state == State.Key) && tokenLen > 0) { // ensure we always have a key in the model after key-marker. Event if the key is empty.
                tokenType = MPropsElements.KEY;
            } else {
                tokenType = null;
            }
            tokenStart = buffer == null ? 0 : buffer.length();
            tokenLen = 0;
            return;
        }
        switch (state) {
            case HeaderComment:
                onHeaderComment();
                break;
            case EolAfterHeaderComment:
                onEolAfterHeaderComment();
                break;
            case KeyMarker:
                onKeyMarker();
                break;
            case WsBeforeKey:
                onWsBeforeKey();
                break;
            case Key:
                onKey();
                break;
            case WsAfterKey:
                onWsAfterKey();
                break;
            case EolAfterKey:
                onEolAfterKey();
                break;
            case Value:
                onValue();
                break;
            case EolAfterValue:
                onEolAfterValue();
                break;
            default:
                tokenType = MPropsElements.BAD_CHARACTER;
        }
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return wholeBuffer;
    }

    @Override
    public int getBufferEnd() {
        return wholeBufferEnd;
    }

    private void onHeaderComment() {
        check(tokenStart == 0, "Start of file");
        if (isKeyMarker(tokenStart)) {
            onKeyMarker();
            return;
        }
        // read header comment
        for (; !isEOF(tokenLen); tokenLen++) {
            if (isNewLineCharFollowedByKeyMarker(tokenLen)) {
                break;
            }
        }
        if (tokenLen == 0) {
            onEolAfterHeaderComment();
            return;
        }
        state = State.EolAfterHeaderComment;
        tokenType = MPropsElements.HEADER_COMMENT;
    }

    private void onEolAfterHeaderComment() {
        advanceToken();
        check(isEOL(tokenStart), "EOL");
        tokenLen = 1;
        check(isEOF(tokenStart + tokenLen) || isKeyMarker(tokenStart + tokenLen), "EOL or key marker");
        state = State.KeyMarker;
        tokenType = MPropsElements.LINE_TERMINATOR;
    }

    private void onKeyMarker() {
        advanceToken();
        check(isKeyMarker(tokenStart), "Key marker");
        tokenLen = 1;
        state = isWS(tokenStart + tokenLen) ? State.WsBeforeKey : State.Key;
        tokenType = MPropsElements.KEY_MARKER;
    }

    private void onWsBeforeKey() {
        advanceToken();
        check(isWS(tokenStart), "WS");
        skipWS();
        check(tokenLen > 0, "tokenLen > 0");
        state = State.Key;
        tokenType = MPropsElements.WHITE_SPACE;
    }

    private void onKey() {
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
        tokenType = MPropsElements.KEY;
    }

    private void onWsAfterKey() {
        advanceToken();
        check(isWS(tokenStart), "WS");

        skipWS();
        check(tokenLen > 0, "tokenLen > 0");
        state = State.EolAfterKey;
        tokenType = MPropsElements.WHITE_SPACE;
    }

    private void onEolAfterKey() {
        advanceToken();
        check(isEOL(tokenStart), "EOL");

        tokenLen = 1;
        state = isKeyMarker(tokenStart + tokenLen) ? State.KeyMarker : State.Value;
        tokenType = MPropsElements.LINE_TERMINATOR;
    }

    private void onValue() {
        advanceToken();
        check(!isKeyMarker(tokenStart), "Not key marker");

        // read the value. Stop on the last line break.
        for (; !isEOF(tokenStart + tokenLen); tokenLen++) {
            if (isNewLineCharFollowedByKeyMarker(tokenStart + tokenLen)) { // the last line terminator is not included into the value
                break;
            }
        }
        if (tokenLen == 0) {
            onEolAfterValue();
            return;
        }
        state = State.EolAfterValue;
        tokenType = MPropsElements.VALUE;
    }

    private void onEolAfterValue() {
        advanceToken();
        check(isEOL(tokenStart), "EOL");

        tokenLen = 1;
        check(isEOF(tokenStart + tokenLen) || isKeyMarker(tokenStart + tokenLen), "EOF or key marker");
        state = State.KeyMarker;
        tokenType = MPropsElements.LINE_TERMINATOR;
    }

    private void advanceToken() {
        check(tokenLen > 0 || tokenStart == 0 || state == State.Value || state == State.EolAfterKey, "Beginning of file or tokenLen > 0 or empty value or empty key");
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
        return idx >= buffer.length();
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


}
