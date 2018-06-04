package com.github.mulpr.parser.util;

public enum TokenAdvanceMode {
    /**
     * Lexer will be advanced for the stop token found.
     */
    ADVANCE,
    /**
     * Lexer will not be advanced for the stop token found, position before stop token will be returned.
     */
    DO_NOT_ADVANCE
}
