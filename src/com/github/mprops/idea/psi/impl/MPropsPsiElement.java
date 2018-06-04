package com.github.mprops.idea.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class MPropsPsiElement extends ASTWrapperPsiElement implements Navigatable {

    public MPropsPsiElement(@NotNull ASTNode node) {
        super(node);
    }

}
