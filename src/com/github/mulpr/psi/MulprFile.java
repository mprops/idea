package com.github.mulpr.psi;

import com.github.mulpr.MulprFileType;
import com.github.mulpr.MulprLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class MulprFile extends PsiFileBase {

    public MulprFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, MulprLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return MulprFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "MULPR File";
    }
}