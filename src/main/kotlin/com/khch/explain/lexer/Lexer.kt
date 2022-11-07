package com.khch.explain.lexer

import com.khch.explain.token.CONST_EOF
import com.khch.explain.token.Token
import com.khch.explain.token.keywordsMap

class Lexer {
    // 代表EOF
    private var ch: Char = CONST_EOF
    private var currentPosition: Int = 0
    private var nextPosition: Int = 0

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
        var token: Token = Token()

        eatWhitespace()

        when (ch) {
            '=' -> {
                token = if (watchNextChar() == '=') {
                    val currentChar = ch
                    readChar()
                    createToken(Token.EQ, currentChar.toString().plus(ch))
                } else {
                    createToken(Token.ASSIGN, ch)
                }
            }

            '!' -> {
                token = if (watchNextChar() == '=') {
                    val currentChar = ch
                    readChar()
                    createToken(Token.NOT_EQ, currentChar.toString().plus(ch))
                } else {
                    createToken(Token.EXCLAMATION, ch)
                }
            }

            ';' -> token = createToken(Token.SEMICOLON, ch)
            '+' -> token = createToken(Token.ADD, ch)
            '-' -> token = createToken(Token.MINUS, ch)
            '*' -> token = createToken(Token.ASTERISK, ch)
            '/' -> token = createToken(Token.SLASH, ch)
            '(' -> token = createToken(Token.LPAREN, ch)
            ')' -> token = createToken(Token.RPAREN, ch)
            '{' -> token = createToken(Token.LBRACE, ch)
            '}' -> token = createToken(Token.RBRACE, ch)
            ',' -> token = createToken(Token.COMMA, ch)
            '>' -> token = createToken(Token.GT, ch)
            '<' -> token = createToken(Token.LT, ch)
            CONST_EOF -> token = createToken(Token.EOF, ch)
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
        val readLetter = readLetter()
        return if (keywordsMap.containsKey(readLetter)) {
            createToken(keywordsMap[readLetter]!!, readLetter)
        } else {
            createToken(Token.IDENT, readLetter)
        }
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

    private fun createToken(tokenType: String, ch: Char): Token {
        return Token(tokenType, ch.toString())
    }

    private fun createToken(tokenType: String, chStr: String): Token {
        return Token(tokenType, chStr)
    }
}