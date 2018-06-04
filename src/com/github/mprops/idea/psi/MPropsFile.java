package com.github.mprops.idea.psi;

import com.github.mprops.idea.MPropsFileType;
import com.github.mprops.idea.MPropsLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class MPropsFile extends PsiFileBase {

    public MPropsFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, MPropsLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return MPropsFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Multiline Properties File";
    }
}