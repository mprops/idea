package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.MPropsIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

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
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiFile containingFile = getContainingFile();
                return containingFile == null ? null : containingFile.getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return MPropsIcons.File;
            }
        };
    }
}
