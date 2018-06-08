package com.github.mprops.idea.navigator;

import com.github.mprops.idea.MPropsLexer;
import com.github.mprops.idea.psi.MPropsElements;
import com.github.mprops.idea.psi.impl.MPropsPropertyElement;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPropsFindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        FlexAdapter lexer = new FlexAdapter(new MPropsLexer());
        return (fileText, processor) -> {
            lexer.start(fileText);
            while (true) {
                IElementType type = lexer.getTokenType();
                if (type == null) {
                    break;
                }
                if (type == MPropsElements.KEY) {
                    WordOccurrence occurrence = new WordOccurrence(lexer.getBufferSequence(), lexer.getTokenStart(), lexer.getTokenEnd(), WordOccurrence.Kind.CODE);
                    processor.process(occurrence);
                }
                lexer.advance();
            }
        };
//        return new DefaultWordsScanner(lexer,
//                TokenSet.create(MPropsElements.KEY),
//                TokenSet.create(MPropsElements.HEADER_COMMENT),
//                TokenSet.EMPTY,
//                TokenSet.create(MPropsElements.LINE_TERMINATOR, MPropsElements.KEY_MARKER)
//        ) {
//            @Override
//            public void processWords(CharSequence fileText, Processor<WordOccurrence> processor) {
//                super.processWords(fileText, processor);
//            }
//
//        };
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return getPropertyElement(psiElement) != null;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        return getPropertyElement(element) != null ? "property" : "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        MPropsPropertyElement property = getPropertyElement(element);
        return property == null ? "" : property.getKey();
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        MPropsPropertyElement property = getPropertyElement(element);
        if (property == null) {
            return "";
        }
        String key = property.getKey();
        String value = property.getValue();

        String formattedKey = StringUtils.abbreviate(key, 30);
        String formatterValue = StringUtils.abbreviate(value.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"), 45);
        if (formatterValue.isEmpty()) {
            formatterValue = "<empty>";
        }
        return formattedKey + " = " + formatterValue;
    }


    @Nullable
    private MPropsPropertyElement getPropertyElement(@Nullable PsiElement psiElement) {
        if (psiElement == null) {
            return null;
        }
        if (psiElement instanceof MPropsPropertyElement) {
            return (MPropsPropertyElement) psiElement;
        }
        PsiElement parent = psiElement.getParent();
        if (parent instanceof MPropsPropertyElement) {
            return (MPropsPropertyElement) parent;
        }
        return null;
    }
}
