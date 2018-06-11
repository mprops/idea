package com.github.mprops.idea.structure;

import com.github.mprops.idea.psi.MPropsFile;
import com.github.mprops.idea.psi.impl.MPropsPropertyElement;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class MPropsStructureViewFileElement extends PsiTreeElementBase<MPropsFile> {

    protected MPropsStructureViewFileElement(@NotNull MPropsFile file) {
        super(file);
    }

    @NotNull
    public Collection<StructureViewTreeElement> getChildrenBase() {
        PsiElement[] children = getFileElement().getChildren();
        return toStructureViewElements(children);
    }

    public static Collection<StructureViewTreeElement> toStructureViewElements(PsiElement[] elements) {
        return Arrays.stream(elements)
                .filter(e -> e instanceof MPropsPropertyElement)
                .map(e -> ((MPropsPropertyElement) e).getKeyElement())
                .filter(Objects::nonNull)
                .map(MPropsStructureViewKeyElement::new)
                .collect(Collectors.toList());
    }

    @NotNull
    public String getPresentableText() {
        return getFileElement().getName();
    }

    @NotNull
    private MPropsFile getFileElement() {
        MPropsFile file = getElement();
        assert file != null;
        return file;
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {

            public String getPresentableText() {
                return MPropsStructureViewFileElement.this.getPresentableText();
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return getFileElement().getIcon(0);
            }
        };
    }
}
