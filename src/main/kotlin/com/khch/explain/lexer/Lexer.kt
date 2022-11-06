package com.khch.explain.lexer

import com.khch.explain.token.CONST_EOF
import com.khch.explain.token.Token
import com.khch.explain.token.keywordsMap

class Lexer {
    // 代表EOF
    var ch: Char = CONST_EOF
    var currentPosition: Int = 0
    var nextPosition: Int = 0

    var str: String = ""

    fun new(input: String) {
        str = input
        readChar()
    }

    private fun readChar() {
        ch = if (nextPosition >= str.length) {
            CONST_EOF
        } else {
            str[nextPosition]
        }
        currentPosition = nextPosition
        nextPosition++
    }

    fun nextToken(): Token {
        var token: Token = Token(null, null)

        eatWhitespace()

        when (ch) {
            '=' -> {
                if (watchNextChar() == '=') {
                    val currentChar = ch
                    readChar()
                    token.tokenType = Token.EQ
                    token.literal = currentChar.toString().plus(ch)
                } else {
                    token.tokenType = Token.ASSIGN
                    token.literal = ch.toString()
                }
            }

            '!' -> {
                if (watchNextChar() == '=') {
                    val currentChar = ch
                    readChar()
                    token.tokenType = Token.NOT_EQ
                    token.literal = currentChar.toString().plus(ch)
                } else {
                    token.tokenType = Token.EXCLAMATION
                    token.literal = ch.toString()
                }
            }

            ';' -> token = Token(Token.SEMICOLON, ch.toString())
            '+' -> token = Token(Token.ADD, ch.toString())
            '-' -> token = Token(Token.MINUS, ch.toString())
            '*' -> token = Token(Token.ASTERISK, ch.toString())
            '/' -> token = Token(Token.SLASH, ch.toString())
            '(' -> token = Token(Token.LPAREN, ch.toString())
            ')' -> token = Token(Token.RPAREN, ch.toString())
            '{' -> token = Token(Token.LBRACE, ch.toString())
            '}' -> token = Token(Token.RBRACE, ch.toString())
            ',' -> token = Token(Token.COMMA, ch.toString())
            '>' -> token = Token(Token.GT, ch.toString())
            '<' -> token = Token(Token.LT, ch.toString())
            CONST_EOF -> token = Token(Token.EOF, ch.toString())
            else -> {
                if (isLetter(ch)) {
                    return getLetter()
                } else if (isDigit(ch)) {
                    return getNumber()
                }
            }
        }
        readChar()

        return token
    }

    private fun watchNextChar(): Char {
        return if (nextPosition >= str.length) {
            CONST_EOF
        } else {
            str[nextPosition]
        }
    }

    private fun getNumber(): Token {
        val readNumber = readNumber()
        return Token(Token.INT, readNumber)
    }

    private fun getLetter(): Token {
        val token: Token = Token(null, null)
        val readLetter = readLetter()
        if (keywordsMap.containsKey(readLetter)) {
            token.tokenType = keywordsMap[readLetter]!!
            token.literal = readLetter
        } else {
            token.tokenType = Token.IDENT
            token.literal = readLetter
        }
        return token
    }

    private fun readLetter(): String {
        val position = currentPosition
        while (isLetter(ch)) {
            readChar()
        }
        return str.substring(position, currentPosition)
    }

    private fun readNumber(): String {
        val position = currentPosition
        while (isDigit(ch)) {
            readChar()
        }
        return str.substring(position, currentPosition)
    }

    private fun eatWhitespace() {
        if (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t') {
            readChar()
        }
    }

    private fun isLetter(ch: Char): Boolean {
        return ch in 'a'..'z' || ch in 'A'..'Z' || ch == '_'
    }

    private fun isDigit(ch: Char): Boolean {
        return ch in '0'..'9'
    }
}