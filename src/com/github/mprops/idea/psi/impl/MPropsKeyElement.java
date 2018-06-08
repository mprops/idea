package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.util.MPropsPresentations;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class MPropsKeyElement extends MPropsPsiElement implements PsiNamedElement {

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

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    @Override
    public ItemPresentation getPresentation() {
        return MPropsPresentations.getPresentation(this);
    }
}
