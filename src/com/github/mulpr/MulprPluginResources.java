package com.github.mulpr;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

public class MulprPluginResources {
    private static Reference<ResourceBundle> MULPR_BUNDLE;

    @NonNls
    private static final String BUNDLE_ID = "messages.PsiBundle";

    private MulprPluginResources() {
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE_ID) String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (MULPR_BUNDLE != null) {
            bundle = MULPR_BUNDLE.get();
        }
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE_ID);
            MULPR_BUNDLE = new SoftReference<>(bundle);
        }
        return bundle;
    }
}
