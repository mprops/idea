package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPropsPropertyElement extends MPropsPsiElement {

    public MPropsPropertyElement(@NotNull ASTNode node) {
        super(node);
    }

    @SuppressWarnings("unused")
    @Nullable
    public PsiElement getKeyMarkerElement() {
        return findChildByType(MPropsElements.KEY_MARKER);
    }

    @Nullable
    public MPropsKeyElement getKeyElement() {
        return findChildByType(MPropsElements.KEY);
    }

    @Nullable
    public PsiElement getValueElement() {
        return findChildByType(MPropsElements.VALUE);
    }

    @NotNull
    public String getKey() {
        PsiElement keyNode = getKeyElement();
        return keyNode == null ? "" : keyNode.getText().trim();
    }

    @NotNull
    public String getValue() {
        PsiElement valueNode = getValueElement();
        return valueNode == null ? "" : valueNode.getText();
    }
}
