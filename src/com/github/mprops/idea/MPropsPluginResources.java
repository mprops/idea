package com.github.mprops.idea;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

public class MPropsPluginResources {

    private static Reference<ResourceBundle> MPROPS_BUNDLE;

    @NonNls
    private static final String BUNDLE_ID = "messages.PsiBundle";

    private MPropsPluginResources() {
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE_ID) String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (MPROPS_BUNDLE != null) {
            bundle = MPROPS_BUNDLE.get();
        }
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE_ID);
            MPROPS_BUNDLE = new SoftReference<>(bundle);
        }
        return bundle;
    }
}
