package com.github.mulpr;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/* Each file in IDEA has type. This is type for Mulpr file. */
public class MulprFileType extends LanguageFileType {

    public static final MulprFileType INSTANCE = new MulprFileType();

    public static final String FILE_EXTENSION = "mulpr";

    private MulprFileType() {
        super(MulprLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "MULPR file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "MULPR language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return MulprIcons.File;
    }
}
