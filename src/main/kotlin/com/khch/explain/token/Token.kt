package com.khch.explain.token

const val CONST_EOF = '\u0000'

enum class TOKEN(var literal: String) {
    //
    IDENT(""),
    ASSIGN("="),
    EQ("=="),
    NOT_EQ("!="),
    INT("int"),

    SEMICOLON(";"),

    // 操作符
    ADD("+"),
    MINUS("-"),
    ASTERISK("*"),
    SLASH("/"),
    GT(">"),
    LT("<"),

    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    COMMA(","),
    EXCLAMATION("!"),

    LET("let"),
    FUNCTION("fn"),
    TRUE("true"),
    FALSE("false"),
    IF("if"),
    ELSE("else"),
    RETURN("return"),

    ILLEGAL("ILLEGAL"),
    EOF(CONST_EOF.toString()),
}

val keywordsMap: Map<String, TOKEN> =
    mapOf(
        Pair("let", TOKEN.LET),
        Pair("fn", TOKEN.FUNCTION),
        Pair("true", TOKEN.TRUE),
        Pair("false", TOKEN.FALSE),
        Pair("if", TOKEN.IF),
        Pair("else", TOKEN.ELSE),
        Pair("return", TOKEN.RETURN),
    )