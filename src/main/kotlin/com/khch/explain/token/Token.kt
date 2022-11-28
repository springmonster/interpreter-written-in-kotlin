package com.khch.explain.token

const val CONST_EOF = '\u0000'

typealias TokenType = String

class Token(var tokenType: TokenType = "", var literal: String = "") {
    companion object {
        const val IDENT: TokenType = "IDENT"
        const val ASSIGN: TokenType = "="
        const val INT: TokenType = "INT"
        const val EQ: TokenType = "=="
        const val NOT_EQ: TokenType = "!="

        const val SEMICOLON: TokenType = ";"

        // 操作符
        const val PLUS: TokenType = "+"
        const val MINUS: TokenType = "-"
        const val ASTERISK: TokenType = "*"
        const val SLASH: TokenType = "/"
        const val GT: TokenType = ">"
        const val LT: TokenType = "<"

        const val LPAREN: TokenType = "("
        const val RPAREN: TokenType = ")"
        const val LBRACE: TokenType = "{"
        const val RBRACE: TokenType = "}"
        const val COMMA: TokenType = ","
        const val BANG: TokenType = "!"

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