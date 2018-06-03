package com.github.mulpr.psi;

import com.github.mulpr.psi.impl.MulprPropertyElement;
import com.github.mulpr.psi.impl.MulprPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MulprElementsFactory implements MulprElements {

    private final static Map<ASTNode, Function<ASTNode, PsiElement>> elementFactory = new HashMap<>();

    public static PsiElement createElement(@NotNull ASTNode node) {
        return elementFactory.computeIfAbsent(node, n -> {
            IElementType type = n.getElementType();
            if (type == PROPERTY) {
                return MulprPropertyElement::new;
            }
            return MulprPsiElement::new;
        }).apply(node);
    }
}
