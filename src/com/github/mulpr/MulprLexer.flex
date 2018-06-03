package com.github.mulpr;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.github.mulpr.psi.MulprElements;

@SuppressWarnings({"ALL"})
%%

%public %class MulprLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

line_terminator = \r|\n|\r\n
white_space_char = [ \t\f]

/*End of rules*/

%%
<YYINITIAL> {

{line_terminator}+  { return MulprElements.LINE_TERMINATOR; }
{white_space_char}+ { return MulprElements.WHITE_SPACE; }

}

[^] { return MulprElements.BAD_CHARACTER; }
