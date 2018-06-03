package com.github.mulpr.psi;

import com.github.mulpr.MulprLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MulprElementType extends IElementType {

    public MulprElementType(@NotNull @NonNls String debugName) {
        super(debugName, MulprLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "MulprElementType." + super.toString();
    }
}
