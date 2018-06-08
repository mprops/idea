package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.util.MPropsPresentations;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.NotNull;

public class MPropsKeyElement extends MPropsPsiElement {

    public MPropsKeyElement(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public String getKey() {
        return getText().trim();
    }

    @Override
    public String getName() {
        String key = getKey();
        return key.isEmpty() ? null : key;
    }

    public String toString() {
        return "key:" + getKey();
    }

    @Override
    public ItemPresentation getPresentation() {
        return MPropsPresentations.getPresentation(this);
    }
}
