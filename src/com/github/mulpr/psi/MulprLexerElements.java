package com.github.mulpr.psi;

import com.intellij.psi.tree.IElementType;

public interface MulprLexerElements {
    IElementType WHITE_SPACE = com.intellij.psi.TokenType.WHITE_SPACE;
    IElementType LINE_TERMINATOR = new MulprElementType("LINE_TERMINATOR");
    IElementType KEY_MARKER = new MulprElementType("KEY_MARKER");
    IElementType BAD_CHARACTER = new MulprElementType("BAD_CHARACTER");
}
