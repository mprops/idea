package com.github.mprops.idea.parser;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import org.jetbrains.annotations.NotNull;

public class MPropsParser implements PsiParser, MPropsElements {

    @NotNull
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder b) {
        doParse(root, b);
        return b.getTreeBuilt();
    }

    @NotNull
    public FlyweightCapableTreeStructure<LighterASTNode> parseLight(IElementType root, PsiBuilder builder) {
        doParse(root, builder);
        return builder.getLightTree();
    }

    private void doParse(@NotNull IElementType root, @NotNull PsiBuilder b) {
        PsiBuilder.Marker fileBlock = b.mark();
        while (!b.eof()) {
            if (b.getTokenType() != KEY_MARKER) {
                b.advanceLexer();
                continue;
            }
            PsiBuilder.Marker propertyBlock = b.mark();
            try {
                b.advanceLexer(); // key marker
                while (!b.eof() && b.getTokenType() != KEY_MARKER) {
                    if (b.getTokenType() == KEY) {
                        PsiBuilder.Marker keyBlock = b.mark();
                        b.advanceLexer();
                        keyBlock.done(KEY);
                    } else {
                        b.advanceLexer();
                    }
                }
            } finally {
                propertyBlock.done(PROPERTY);
            }
        }
        fileBlock.done(root);
    }
}
