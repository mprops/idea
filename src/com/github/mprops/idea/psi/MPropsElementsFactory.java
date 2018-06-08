package com.github.mprops.idea.psi;

import com.github.mprops.idea.psi.impl.MPropsKeyElement;
import com.github.mprops.idea.psi.impl.MPropsPropertyElement;
import com.github.mprops.idea.psi.impl.MPropsPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MPropsElementsFactory implements MPropsElements {

    private final static Map<ASTNode, Function<ASTNode, PsiElement>> FACTORIES = new HashMap<>();

    @NotNull
    public static PsiElement createElement(@NotNull ASTNode node) {
        return FACTORIES.computeIfAbsent(node, n -> {
            IElementType type = n.getElementType();
            if (type == PROPERTY) {
                return MPropsPropertyElement::new;
            } else if (type == KEY) {
                return MPropsKeyElement::new;
            }
            return MPropsPsiElement::new;
        }).apply(node);
    }
}
