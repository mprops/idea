package com.github.mulpr.editor;

import com.github.mulpr.MulprLexer;
import com.github.mulpr.psi.MulprElements;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Rules to highlight MULPR text.
 */
public class MulprSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey[]> KEYS_BY_TYPE = new HashMap<>();

    static {
        put(MulprElements.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
        put(MulprElements.KEY, DefaultLanguageHighlighterColors.STRING);
        put(MulprElements.VALUE, DefaultLanguageHighlighterColors.STRING);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new MulprLexer(null));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return KEYS_BY_TYPE.computeIfAbsent(tokenType, tt -> new TextAttributesKey[0]);
    }

    private static void put(@NotNull IElementType e, @NotNull TextAttributesKey... tokens) {
        KEYS_BY_TYPE.put(e, tokens);
    }

    public static class MulprSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
        @NotNull
        @Override
        public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
            return new MulprSyntaxHighlighter();
        }
    }
}
