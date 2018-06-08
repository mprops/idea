package com.github.mprops.idea.editor;

import com.github.mprops.idea.psi.MPropsElements;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MPropsFoldingBuilder implements FoldingBuilder, DumbAware {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = new ArrayList<>();
        collectDescriptorsRecursively(node, descriptors);
        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    private void collectDescriptorsRecursively(@NotNull ASTNode node, @NotNull List<FoldingDescriptor> descriptors) {
        IElementType type = node.getElementType();
        if (type == MPropsElements.PROPERTY) {
            String key = findChildText(node, MPropsElements.KEY);
            if (!key.isEmpty()) {
                descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
            }
        } else if (type == MPropsElements.HEADER_COMMENT) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }
        for (ASTNode child : node.getChildren(null)) {
            collectDescriptorsRecursively(child, descriptors);
        }
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        IElementType type = node.getElementType();
        if (type == MPropsElements.PROPERTY) {
            String key = findChildText(node, MPropsElements.KEY);
            String value = findChildText(node, MPropsElements.VALUE);
            String formattedKey = StringUtils.abbreviate(key, 30);
            String formatterValue = StringUtils.abbreviate(value.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"), 45);
            if (formatterValue.isEmpty()) {
                formatterValue = "<empty>";
            }
            return "~ " + formattedKey + " = " + formatterValue;
        } else if (type == MPropsElements.HEADER_COMMENT) {
            return "/*...*/";
        }
        return "...";
    }

    @NotNull
    private String findChildText(@NotNull ASTNode node, @NotNull IElementType type) {
        ASTNode[] children = node.getChildren(TokenSet.create(type));
        return children.length == 0 ? "" : children[0].getText();
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return node.getElementType() == MPropsElements.HEADER_COMMENT;
    }
}
