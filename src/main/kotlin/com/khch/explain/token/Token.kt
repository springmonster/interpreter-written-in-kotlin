package com.khch.explain.token

const val CONST_EOF = '\u0000'

class Token(var tokenType: String = "", var literal: String = "") {
    companion object {
        const val IDENT = "IDENT"
        const val ASSIGN = "ASSIGN"
        const val INT = "INT"
        const val EQ = "EQ"
        const val NOT_EQ = "NOT_EQ"

        const val SEMICOLON = "SEMICOLON"

        // 操作符
        const val ADD = "ADD"
        const val MINUS = "MINUS"
        const val ASTERISK = "ASTERISK"
        const val SLASH = "SLASH"
        const val GT = "GT"
        const val LT = "LT"

        const val LPAREN = "LPAREN"
        const val RPAREN = "RPAREN"
        const val LBRACE = "LBRACE"
        const val RBRACE = "RBRACE"
        const val COMMA = "COMMA"
        const val EXCLAMATION = "EXCLAMATION"

        const val LET = "LET"
        const val FUNCTION = "FUNCTION"
        const val TRUE = "TRUE"
        const val FALSE = "FALSE"
        const val IF = "IF"
        const val ELSE = "ELSE"
        const val RETURN = "RETURN"

        const val ILLEGAL = "ILLEGAL"
        const val EOF = "EOF"
    }
}

val keywordsMap: Map<String, String> =
    mapOf(
        Pair("let", Token.LET),
        Pair("fn", Token.FUNCTION),
        Pair("true", Token.TRUE),
        Pair("false", Token.FALSE),
        Pair("if", Token.IF),
        Pair("else", Token.ELSE),
        Pair("return", Token.RETURN),
    )