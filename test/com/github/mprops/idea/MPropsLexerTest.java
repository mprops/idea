package com.github.mprops.idea;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MPropsLexerTest extends Assert implements MPropsElements {

    @Test
    public void testEmptyInput() {
        List<P> positions = getPositions("");
        assertEquals(new ArrayList<>(), positions);
    }

    @Test
    public void testHeaderComment() {
        List<P> positions = getPositions("Hello");
        assertEquals(list(new P(HEADER_COMMENT, 0, 5)), positions);
    }

    @Test
    public void testKeyMarkerOnly() {
        List<P> positions = getPositions("~");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 1)
        ), positions);
    }

    @Test
    public void testKeyMarkerAndSpace() {
        List<P> positions = getPositions("~ ");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(WHITE_SPACE, 1, 2),
                new P(KEY, 2, 2)
        ), positions);
    }

    @Test
    public void testKeyMarkerAndOel() {
        List<P> positions = getPositions("~\n");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 1),
                new P(LINE_TERMINATOR, 1, 2)
        ), positions);
    }

    @Test
    public void testKeyMarkerAndSpaceAndOel() {
        List<P> positions = getPositions("~ \n");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(WHITE_SPACE, 1, 2),
                new P(KEY, 2, 2),
                new P(LINE_TERMINATOR, 2, 3)
        ), positions);
    }

    @Test
    public void testKeyOnly() {
        List<P> positions = getPositions("~key");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 4)
        ), positions);
    }

    @Test
    public void testKeyOnlyAndEol() {
        List<P> positions = getPositions("~key\n");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 4),
                new P(LINE_TERMINATOR, 4, 5)
        ), positions);
    }

    @Test
    public void testNoValue() {
        List<P> positions = getPositions("~key1\n~key2");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 5),
                new P(LINE_TERMINATOR, 5, 6),
                new P(KEY_MARKER, 6, 7),
                new P(KEY, 7, 11)
        ), positions);
    }

    @Test
    public void testValue() {
        List<P> positions = getPositions("~key\nvalue");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 4),
                new P(LINE_TERMINATOR, 4, 5),
                new P(VALUE, 5, 10)
        ), positions);
    }

    @Test
    public void testValueWithEol() {
        List<P> positions = getPositions("~key\nvalue\n");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 4),
                new P(LINE_TERMINATOR, 4, 5),
                new P(VALUE, 5, 11)
        ), positions);
    }

    @Test
    public void testValueOfEol() {
        List<P> positions = getPositions("~key\n\n");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 4),
                new P(LINE_TERMINATOR, 4, 5),
                new P(VALUE, 5, 6)
        ), positions);
    }

    @Test
    public void testWsAroundKey() {
        List<P> positions = getPositions("~ ~key~ ");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(WHITE_SPACE, 1, 2),
                new P(KEY, 2, 7),
                new P(WHITE_SPACE, 7, 8)
        ), positions);
    }

    @Test
    public void testKeyWithSpaces() {
        List<P> positions = getPositions("~ a b ");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(WHITE_SPACE, 1, 2),
                new P(KEY, 2, 5),
                new P(WHITE_SPACE, 5, 6)
        ), positions);
    }

    @Test
    public void testKeyWithSpacesAndEol() {
        List<P> positions = getPositions("~ a b \n");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(WHITE_SPACE, 1, 2),
                new P(KEY, 2, 5),
                new P(WHITE_SPACE, 5, 6),
                new P(LINE_TERMINATOR, 6, 7)
        ), positions);
    }

    @Test
    public void testKeyValueEndsCorrectly() {
        List<P> positions = getPositions("~k\nv\n~");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 2),
                new P(LINE_TERMINATOR, 2, 3),
                new P(VALUE, 3, 4),
                new P(LINE_TERMINATOR, 4, 5),
                new P(KEY_MARKER, 5, 6),
                new P(KEY, 6, 6)
        ), positions);
    }

    @Test
    public void testKeyValueCanContainKeyMarkers() {
        List<P> positions = getPositions("~k\nv\n ~");
        assertEquals(list(
                new P(KEY_MARKER, 0, 1),
                new P(KEY, 1, 2),
                new P(LINE_TERMINATOR, 2, 3),
                new P(VALUE, 3, 7)
        ), positions);
    }

    @NotNull
    private static List<P> getPositions(@NotNull final String text) {
        MPropsLexer lexer = new MPropsLexer();
        lexer.start(text, 0, text.length(), 0);
        List<P> positions = new ArrayList<>();
        while (true) {
            lexer.advance();
            IElementType tokenType = lexer.getTokenType();
            if (tokenType == null) {
                break;
            }
            positions.add(new P(tokenType, lexer.getTokenStart(), lexer.getTokenEnd()));
        }
        return positions;
    }

    /**
     * Helper method that uses the same list type as getPositions (easier to check toString method).
     */
    @NotNull
    private static List<P> list(P... ps) {
        List<P> result = new ArrayList<>();
        Collections.addAll(result, ps);
        return result;
    }

    private static class P {
        public IElementType tokenType;
        public int tokenStart;
        public int tokenEnd;

        public P(IElementType tokenType, int tokenStart, int tokenEnd) {
            this.tokenType = tokenType;
            this.tokenStart = tokenStart;
            this.tokenEnd = tokenEnd;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            P p = (P) o;
            return tokenStart == p.tokenStart &&
                    tokenEnd == p.tokenEnd &&
                    Objects.equals(tokenType, p.tokenType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tokenType, tokenStart, tokenEnd);
        }

        @Override
        public String toString() {
            return "P[" + tokenType + "(" + tokenStart + "," + tokenEnd + ")]";
        }
    }

}