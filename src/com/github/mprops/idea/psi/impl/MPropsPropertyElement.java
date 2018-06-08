package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.psi.MPropsElements;
import com.github.mprops.idea.util.MPropsPresentations;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class MPropsPropertyElement extends MPropsPsiElement {

    public MPropsPropertyElement(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    public PsiElement getKeyMarker() {
        return findChildByType(MPropsElements.KEY_MARKER);
    }

    @Nullable
    public PsiElement getKeyNode() {
        return findChildByType(MPropsElements.KEY);
    }

    @Nullable
    public PsiElement getValueNode() {
        return findChildByType(MPropsElements.VALUE);
    }

    @NotNull
    public String getKey() {
        PsiElement keyNode = getKeyNode();
        return keyNode == null ? "" : keyNode.getText().trim();
    }

    @NotNull
    public String getValue() {
        PsiElement valueNode = getValueNode();
        return valueNode == null ? "" : valueNode.getText();
    }

    @Override
    public String toString() {
        return "#property";
    }

    @Override
    public String getName() {
        String key = getKey();
        return key.isEmpty() ? null : key;
    }

    @Override
    public ItemPresentation getPresentation() {
        return MPropsPresentations.getPresentation(this);
    }
}
