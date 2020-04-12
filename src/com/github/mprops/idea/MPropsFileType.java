package com.github.mprops.idea;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/* Each file in IDEA has type. This is type for multiline properties file. */
public class MPropsFileType extends LanguageFileType {

    public static final MPropsFileType INSTANCE = new MPropsFileType();

    public static final String FILE_EXTENSION = "mproperties";

    private MPropsFileType() {
        super(MPropsLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Multiline Props File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Multiline properties file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return MPropsIcons.File;
    }
}
