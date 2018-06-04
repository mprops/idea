package com.github.mprops.idea.psi;

import com.github.mprops.idea.psi.stub.MPropsStubElements;
import com.intellij.psi.tree.IElementType;

public interface MPropsElements extends MPropsStubElements {

    IElementType HEADER_COMMENT = new MPropsElementType("HEADER_COMMENT");

    IElementType KEY_MARKER = new MPropsElementType("KEY_MARKER");
    IElementType KEY = new MPropsElementType("KEY");
    IElementType VALUE = new MPropsElementType("VALUE");

    IElementType PROPERTY = new MPropsElementType("PROPERTY");

    IElementType BAD_CHARACTER = new MPropsElementType("BAD_CHARACTER");
}
