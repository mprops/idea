package com.github.mulpr.parser;

import com.github.mulpr.MulprLexer;
import com.github.mulpr.psi.MulprElements;
import com.github.mulpr.psi.MulprElementsFactory;
import com.github.mulpr.psi.MulprFile;
import com.github.mulpr.psi.stub.MulprStubElements;
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

/* Parser definition used by IntelliJ Platform to parse MULPR file. */
public class MulprParserDefinition implements ParserDefinition, MulprElements {

    public static final TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE, LINE_TERMINATOR);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new MulprLexer());
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new MulprParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return MulprStubElements.FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new MulprFile(viewProvider);
    }

    @NotNull
    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return MulprElementsFactory.createElement(node);
    }
}
