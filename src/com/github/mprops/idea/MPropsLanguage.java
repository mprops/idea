package com.github.mprops.idea;

import com.intellij.lang.Language;

public class MPropsLanguage extends Language {
    public static final MPropsLanguage INSTANCE = new MPropsLanguage();

    private MPropsLanguage() {
        super("MProps");
    }
}
