package com.github.mprops.idea.util;

import com.github.mprops.idea.MPropsFileType;
import com.github.mprops.idea.psi.MPropsFile;
import com.github.mprops.idea.psi.impl.MPropsKeyElement;
import com.github.mprops.idea.psi.impl.MPropsPropertyElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MPropsUtils {

    @NotNull
    public static List<MPropsPropertyElement> findProperties(@NotNull Project project, @Nullable String key) {
        List<MPropsPropertyElement> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(MPropsFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            MPropsFile mPropsFile = (MPropsFile) PsiManager.getInstance(project).findFile(virtualFile);
            List<MPropsPropertyElement> properties = mPropsFile == null ? null : PsiTreeUtil.getChildrenOfTypeAsList(mPropsFile, MPropsPropertyElement.class);
            if (properties == null) {
                continue;
            }
            if (key == null) {
                result.addAll(properties);
            } else {
                result.addAll(properties.stream().filter(p -> key.equals(p.getKey())).collect(Collectors.toList()));
            }
        }
        return result;
    }

}