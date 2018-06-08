package com.github.mprops.idea.util;

import com.github.mprops.idea.MPropsIcons;
import com.github.mprops.idea.psi.impl.MPropsKeyElement;
import com.github.mprops.idea.psi.impl.MPropsPropertyElement;
import com.github.mprops.idea.psi.impl.MPropsPsiElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class MPropsPresentations {

    @Nullable
    public static ItemPresentation getPresentation(@NotNull MPropsPsiElement e) {
        if (!(e instanceof MPropsPropertyElement || e instanceof MPropsKeyElement)) {
            return null; // something unexpected? (a point for breakpoint)
        }
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return e.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiFile containingFile = e.getContainingFile();
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
