package com.github.mulpr;

import com.intellij.lang.Language;

public class MulprLanguage extends Language {
    public static final MulprLanguage INSTANCE = new MulprLanguage();

    private MulprLanguage() {
        super("MULPR");
    }
}
