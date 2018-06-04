package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPropsPropertyElement extends MPropsPsiElement {

    @Nullable
    public ASTNode keyMarker;

    @Nullable
    public ASTNode keyNode;

    @Nullable
    public ASTNode valueNode;

    public MPropsPropertyElement(@NotNull ASTNode node) {
        super(node);
    }

    public void sync() {
        ASTNode node = getNode();
        keyMarker = node.findChildByType(MPropsElements.KEY_MARKER);
        keyNode = node.findChildByType(MPropsElements.KEY);
        valueNode = node.findChildByType(MPropsElements.VALUE);
    }

    @Override
    public String toString() {
        return "#property";
    }
}
