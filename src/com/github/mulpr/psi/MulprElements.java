package com.github.mulpr.psi;

import com.github.mulpr.psi.stub.MulprStubElements;
import com.intellij.psi.tree.IElementType;

public interface MulprElements extends MulprStubElements, MulprLexerElements {
    IElementType PROPERTY = new MulprElementType("PROPERTY");
    IElementType KEY = new MulprElementType("KEY");
    IElementType VALUE = new MulprElementType("VALUE");
}
