package com.github.mprops.idea.psi.stub;

import com.github.mprops.idea.MPropsLanguage;
import com.github.mprops.idea.parser.MPropsParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILightStubFileElementType;
import com.intellij.util.diff.FlyweightCapableTreeStructure;

public interface MPropsStubElements {

    @SuppressWarnings("unused")
    int STUB_SCHEMA_VERSION = 1;

    ILightStubFileElementType FILE = new ILightStubFileElementType(MPropsLanguage.INSTANCE) {
        public FlyweightCapableTreeStructure<LighterASTNode> parseContentsLight(ASTNode chameleon) {
            PsiElement psi = chameleon.getPsi();
            assert (psi != null) : ("Bad chameleon: " + chameleon);

            Project project = psi.getProject();
            PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
            PsiBuilder builder = factory.createBuilder(project, chameleon);
            ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(getLanguage());
            assert (parserDefinition != null) : this;
            MPropsParser parser = new MPropsParser();
            return parser.parseLight(this, builder);
        }
    };
}
