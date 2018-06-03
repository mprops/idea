package com.github.mulpr.psi.impl;

import com.github.mulpr.psi.MulprElements;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MulprPropertyElement extends MulprPsiElement {

    @Nullable
    public ASTNode keyMarker;

    @Nullable
    public ASTNode keyNode;

    @Nullable
    public ASTNode valueNode;

    public MulprPropertyElement(@NotNull ASTNode node) {
        super(node);
    }

    public void sync() {
        ASTNode node = getNode();
        keyMarker = node.findChildByType(MulprElements.KEY_MARKER);
        keyNode = node.findChildByType(MulprElements.KEY);
        valueNode = node.findChildByType(MulprElements.VALUE);
    }

    @Override
    public String toString() {
        return "#property";
    }
}
