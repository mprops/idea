package com.github.mulpr.action;

import com.github.mulpr.MulprFileType;
import com.github.mulpr.MulprIcons;
import com.github.mulpr.MulprPluginResources;
import com.intellij.ide.actions.CreateFileAction;

/**
 * The "New MULPR File" action.
 */
public class CreateMulprFileAction extends CreateFileAction {
    public CreateMulprFileAction() {
        super(MulprPluginResources.message("action.New-Mulpr-File.text"), MulprPluginResources.message("action.New-Mulpr-File.text"), MulprIcons.File);
    }

    @Override
    protected String getDefaultExtension() {
        return MulprFileType.FILE_EXTENSION;
    }
}
