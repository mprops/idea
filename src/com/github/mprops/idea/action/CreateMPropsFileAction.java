package com.github.mprops.idea.action;

import com.github.mprops.idea.MPropsFileType;
import com.github.mprops.idea.MPropsIcons;
import com.github.mprops.idea.MPropsPluginResources;
import com.intellij.ide.actions.CreateFileAction;

/**
 * The "New Multiline Properties File" action.
 */
public class CreateMPropsFileAction extends CreateFileAction {
    public CreateMPropsFileAction() {
        super(MPropsPluginResources.message("action.New-MProps-File.text"), MPropsPluginResources.message("action.New-MProps-File.text"), MPropsIcons.File);
    }

    @Override
    protected String getDefaultExtension() {
        return MPropsFileType.FILE_EXTENSION;
    }
}
