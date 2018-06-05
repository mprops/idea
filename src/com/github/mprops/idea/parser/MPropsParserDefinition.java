package com.github.mprops.idea.parser;

import com.github.mprops.idea.MPropsLexer;
import com.github.mprops.idea.psi.MPropsElements;
import com.github.mprops.idea.psi.MPropsElementsFactory;
import com.github.mprops.idea.psi.MPropsFile;
import com.github.mprops.idea.psi.stub.MPropsStubElements;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/* Parser definition used by IntelliJ Platform to parse multiline properties file. */
public class MPropsParserDefinition implements ParserDefinition, MPropsElements {

    public static final TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE, LINE_TERMINATOR);

    public static final TokenSet COMMENTS = TokenSet.create(HEADER_COMMENT);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new MPropsLexer());
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new MPropsParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return MPropsStubElements.FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new MPropsFile(viewProvider);
    }

    @NotNull
    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return MPropsElementsFactory.createElement(node);
    }
}
