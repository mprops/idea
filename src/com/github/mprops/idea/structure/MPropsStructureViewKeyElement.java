package com.github.mprops.idea.structure;

import com.github.mprops.idea.psi.impl.MPropsKeyElement;
import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.NotNull;


public class MPropsStructureViewKeyElement extends MPropsStructureViewElement<MPropsKeyElement> {

    public MPropsStructureViewKeyElement(@NotNull MPropsKeyElement element) {
        super(element);
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return element.getPresentation();
    }
}
