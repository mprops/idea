package com.github.mulpr.parser.util;

import com.github.mulpr.psi.MulprElements;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParsingUtils implements MulprElements {

    public static void repeat(int n, Runnable r) {
        for (int i = 0; i < n; i++) {
            r.run();
        }
    }

    public static IElementType advanceLexer(@NotNull PsiBuilder b, int n) {
        repeat(n, b::advanceLexer);
        return b.getTokenType();
    }

    public static IElementType advanceLexer(@NotNull PsiBuilder b) {
        b.advanceLexer();
        return b.getTokenType();
    }

    //TODO: optimize this method: pass from & to idx
    public static boolean containsEndOfLine(@Nullable String text) {
        return text != null && text.contains("\n");
    }


    /**
     * @return true if there is new line between startPos and currentPos or currentPos is EOF.
     */
    public static boolean containsEndOfLineOrFile(@NotNull PsiBuilder b, int startPos) {
        return containsEndOfLine(b, startPos) || b.getOriginalText().length() == b.getCurrentOffset();
    }

    /**
     * @return true if there is new line between startPos and currentPos.
     */
    private static boolean containsEndOfLine(@NotNull PsiBuilder b, int startPos) {
        String text = b.getOriginalText().subSequence(startPos, b.getCurrentOffset()).toString();
        return containsEndOfLine(text);
    }

    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull TokenSet stopTypes, TokenAdvanceMode advanceStopTokens) {
        if (advanceStopTokens == TokenAdvanceMode.ADVANCE) {
            return advanceLexerUntil(b, stopTypes, TokenSet.EMPTY);
        } else {
            return advanceLexerUntil(b, TokenSet.EMPTY, stopTypes);
        }
    }

    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull TokenSet stopTypesAdvance, TokenSet stopTypesDoNotAdvance) {
        b.setTokenTypeRemapper((t, start, end, text) -> stopTypesAdvance.contains(t) || stopTypesDoNotAdvance.contains(t) ? ParsingMarker.forType(t) : t);
        try {
            // find the token
            while (!(b.getTokenType() instanceof ParsingMarker)) {
                b.advanceLexer();
                if (b.eof()) {
                    return false;
                }
            }
            // restore original token, remove marker and advance if needed.
            do {
                ParsingMarker m = (ParsingMarker) b.getTokenType();
                b.remapCurrentToken(m.originalToken);
                if (stopTypesDoNotAdvance.contains(m.originalToken)) {
                    break;
                }
                b.advanceLexer();
            } while (!b.eof() && b.getTokenType() instanceof ParsingMarker);
            return true;
        } finally {
            b.setTokenTypeRemapper(null);
        }
    }


    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull IElementType type, @NotNull TokenAdvanceMode mode) {
        return advanceLexerUntil(b, TokenSet.create(type), mode);
    }


}
