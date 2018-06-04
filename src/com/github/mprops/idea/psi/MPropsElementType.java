package com.github.mprops.idea.psi;

import com.github.mprops.idea.MPropsLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MPropsElementType extends IElementType {

    public MPropsElementType(@NotNull @NonNls String debugName) {
        super(debugName, MPropsLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "MPropsElementType." + super.toString();
    }
}
