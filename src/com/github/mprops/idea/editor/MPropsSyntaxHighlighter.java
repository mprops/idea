package com.github.mprops.idea.editor;

import com.github.mprops.idea.MPropsLexer;
import com.github.mprops.idea.psi.MPropsElements;
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
 * Rules to highlight multiline properties..
 */
public class MPropsSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey[]> KEYS_BY_TYPE = new HashMap<>();

    static {
        put(MPropsElements.HEADER_COMMENT, DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        put(MPropsElements.KEY_MARKER, DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
        put(MPropsElements.KEY, DefaultLanguageHighlighterColors.KEYWORD);
        put(MPropsElements.VALUE, DefaultLanguageHighlighterColors.STRING);
        put(MPropsElements.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);

    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new MPropsLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return KEYS_BY_TYPE.computeIfAbsent(tokenType, tt -> new TextAttributesKey[0]);
    }

    private static void put(@NotNull IElementType e, @NotNull TextAttributesKey... tokens) {
        KEYS_BY_TYPE.put(e, tokens);
    }

    public static class MPropsSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
        @NotNull
        @Override
        public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
            return new MPropsSyntaxHighlighter();
        }
    }
}
