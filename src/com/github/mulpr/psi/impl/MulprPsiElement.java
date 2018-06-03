package com.github.mulpr.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class MulprPsiElement extends ASTWrapperPsiElement implements Navigatable {

    public MulprPsiElement(@NotNull ASTNode node) {
        super(node);
    }

}
