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

    FUNCTION("func"),
    LET("let"),
    RETURN("return"),

    ILLEGAL("ILLEGAL"),
    EOF(CONST_EOF.toString()),
}

val keywordsMap: Map<String, TOKEN> =
    mapOf(
        Pair("let", TOKEN.LET),
        Pair("int", TOKEN.INT),
        Pair("func", TOKEN.FUNCTION),
        Pair("return", TOKEN.RETURN)
    )