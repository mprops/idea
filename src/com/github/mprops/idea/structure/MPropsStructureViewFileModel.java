package com.github.mprops.idea.structure;

import com.github.mprops.idea.psi.MPropsFile;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import org.jetbrains.annotations.NotNull;

public class MPropsStructureViewFileModel extends TextEditorBasedStructureViewModel {
    private final MPropsFile file;

    public MPropsStructureViewFileModel(final MPropsFile file) {
        super(file);
        this.file = file;
    }

    @NotNull
    public StructureViewTreeElement getRoot() {
        return new MPropsStructureViewFileElement(file);
    }
}
