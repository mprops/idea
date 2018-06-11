package com.github.mprops.idea.structure;

import com.github.mprops.idea.psi.MPropsFile;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPropsStructureViewBuilderFactory implements PsiStructureViewFactory {
    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
        return (fileEditor, project) -> new MPropsStructureViewFileComponent(project, (MPropsFile) psiFile, fileEditor);
    }
}
