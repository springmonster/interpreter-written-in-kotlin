package com.khch.explain.token

const val CONST_EOF = '\u0000'

class Token(var tokenType: String?, var literal: String?) {
    companion object {
        val IDENT = "IDENT"
        val ASSIGN = "ASSIGN"
        val INT = "INT"
        val EQ = "EQ"
        val NOT_EQ = "NOT_EQ"

        val SEMICOLON = "SEMICOLON"

        // 操作符
        val ADD = "ADD"
        val MINUS = "MINUS"
        val ASTERISK = "ASTERISK"
        val SLASH = "SLASH"
        val GT = "GT"
        val LT = "LT"

        val LPAREN = "LPAREN"
        val RPAREN = "RPAREN"
        val LBRACE = "LBRACE"
        val RBRACE = "RBRACE"
        val COMMA = "COMMA"
        val EXCLAMATION = "EXCLAMATION"

        val LET = "LET"
        val FUNCTION = "FUNCTION"
        val TRUE = "TRUE"
        val FALSE = "FALSE"
        val IF = "IF"
        val ELSE = "ELSE"
        val RETURN = "RETURN"

        val ILLEGAL = "ILLEGAL"
        val EOF = "EOF"
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