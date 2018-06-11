package com.github.mprops.idea.psi.impl;

import com.github.mprops.idea.MPropsIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

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

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @NotNull
            @Override
            public String getPresentableText() {
                String name = getName();
                return name == null || name.isEmpty() ? "<empty>" : name;
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
