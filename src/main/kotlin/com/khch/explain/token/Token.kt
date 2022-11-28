package com.khch.explain.token

const val CONST_EOF = '\u0000'

typealias TokenType = String

class Token(var tokenType: TokenType = "", var literal: String = "") {
    companion object {
        const val IDENT: TokenType = "IDENT"
        const val ASSIGN: TokenType = "ASSIGN"
        const val INT: TokenType = "INT"
        const val EQ: TokenType = "EQ"
        const val NOT_EQ: TokenType = "NOT_EQ"

        const val SEMICOLON: TokenType = "SEMICOLON"

        // 操作符
        const val ADD: TokenType = "ADD"
        const val MINUS: TokenType = "MINUS"
        const val ASTERISK: TokenType = "ASTERISK"
        const val SLASH: TokenType = "SLASH"
        const val GT: TokenType = "GT"
        const val LT: TokenType = "LT"

        const val LPAREN: TokenType = "LPAREN"
        const val RPAREN: TokenType = "RPAREN"
        const val LBRACE: TokenType = "LBRACE"
        const val RBRACE: TokenType = "RBRACE"
        const val COMMA: TokenType = "COMMA"
        const val EXCLAMATION: TokenType = "EXCLAMATION"

        const val LET: TokenType = "LET"
        const val FUNCTION: TokenType = "FUNCTION"
        const val TRUE: TokenType = "TRUE"
        const val FALSE: TokenType = "FALSE"
        const val IF: TokenType = "IF"
        const val ELSE: TokenType = "ELSE"
        const val RETURN: TokenType = "RETURN"

        const val ILLEGAL: TokenType = "ILLEGAL"
        const val EOF: TokenType = "EOF"
    }
}

val keywordsMap: Map<String, TokenType> =
    mapOf(
        Pair("let", Token.LET),
        Pair("fn", Token.FUNCTION),
        Pair("true", Token.TRUE),
        Pair("false", Token.FALSE),
        Pair("if", Token.IF),
        Pair("else", Token.ELSE),
        Pair("return", Token.RETURN),
    )