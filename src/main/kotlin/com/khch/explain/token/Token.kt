package com.khch.explain.token

const val CONST_EOF = (0).toChar()

enum class TOKEN(var value: String) {
    //
    IDENT(""),
    EQUALS("="),
    INT("int"),

    SEMICOLON(";"),

    // 操作符
    ADD("+"),
    MINUS("-"),
    ASTERISK("*"),
    SLASH("/"),

    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    COMMA(","),

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
        Pair("int", TOKEN.INT),
        Pair("fn", TOKEN.FUNCTION),
        Pair("true", TOKEN.TRUE),
        Pair("false", TOKEN.FALSE),
        Pair("if", TOKEN.IF),
        Pair("else", TOKEN.ELSE),
        Pair("return", TOKEN.RETURN),
    )