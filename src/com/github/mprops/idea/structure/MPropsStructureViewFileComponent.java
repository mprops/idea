package com.github.mprops.idea.structure;

import com.github.mprops.idea.psi.MPropsFile;
import com.intellij.ide.structureView.newStructureView.StructureViewComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;

public class MPropsStructureViewFileComponent extends StructureViewComponent {

    public MPropsStructureViewFileComponent(Project project, MPropsFile file, FileEditor fileEditor) {
        super(fileEditor, new MPropsStructureViewFileModel(file), project, true);
    }
}