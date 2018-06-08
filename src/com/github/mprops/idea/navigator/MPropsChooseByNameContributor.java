package com.github.mprops.idea.navigator;

import com.github.mprops.idea.psi.impl.MPropsPropertyElement;
import com.github.mprops.idea.util.MPropsUtils;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MPropsChooseByNameContributor implements ChooseByNameContributor {

    @NotNull
    @Override
    public String[] getNames(@NotNull Project project, boolean includeNonProjectItems) {
        List<MPropsPropertyElement> properties = MPropsUtils.findProperties(project, null);
        return properties.stream().map(MPropsPropertyElement::getKey).filter(n -> !n.isEmpty()).toArray(String[]::new);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(@NotNull String name, @NotNull String pattern, @NotNull Project project, boolean includeNonProjectItems) {
        List<MPropsPropertyElement> properties = MPropsUtils.findProperties(project, name);
        return properties.stream()
                .map(MPropsPropertyElement::getKeyElement)
                .filter(Objects::nonNull)
                .toArray(NavigationItem[]::new);
    }
}
