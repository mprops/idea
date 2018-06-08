package com.github.mprops.idea.navigator;

import com.github.mprops.idea.MPropsLexer;
import com.github.mprops.idea.psi.MPropsElements;
import com.github.mprops.idea.psi.impl.MPropsKeyElement;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
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
                    WordOccurrence o = new WordOccurrence(lexer.getBufferSequence(), lexer.getTokenStart(), lexer.getTokenEnd(), WordOccurrence.Kind.CODE);
                    processor.process(o);
                }
                lexer.advance();
            }
        };
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        MPropsKeyElement e = getKeyElement(psiElement);
        return e != null && !e.getKey().isEmpty();
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement e) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement e) {
        return getKeyElement(e) != null ? "property" : "???";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        MPropsKeyElement e = getKeyElement(element);
        return e == null ? "" : e.getKey();
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        MPropsKeyElement e = getKeyElement(element);
        return e == null ? "" : e.getKey();
    }


    @Nullable
    private MPropsKeyElement getKeyElement(@Nullable PsiElement e) {
        if (e == null || e instanceof MPropsKeyElement) {
            return (MPropsKeyElement) e;
        }
//TODO: does not work on key-marker or on whitespaces around key.
//        if (e instanceof MPropsPropertyElement) {
//            return ((MPropsPropertyElement) e).getKeyElement();
//        }
        return getKeyElement(e.getParent());
    }
}
