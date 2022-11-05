package com.khch.explain.lexer

import com.khch.explain.token.CONST_EOF
import com.khch.explain.token.TOKEN
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

    fun analyze(): TOKEN {
        var token = TOKEN.EOF

        eatWhitespace()

        when (ch) {
            '=' -> {
                if (watchNextChar() == '=') {
                    val currentChar = ch
                    readChar()
                    token = TOKEN.EQ
                    token.value = currentChar.toString().plus(ch)
                } else {
                    token = TOKEN.ASSIGN
                }
            }

            '!' -> {
                if (watchNextChar() == '=') {
                    val currentChar = ch
                    readChar()
                    token = TOKEN.NOT_EQ
                    token.value = currentChar.toString().plus(ch)
                } else {
                    token = TOKEN.EXCLAMATION
                }
            }

            ';' -> token = TOKEN.SEMICOLON
            '+' -> token = TOKEN.ADD
            '-' -> token = TOKEN.MINUS
            '*' -> token = TOKEN.ASTERISK
            '/' -> token = TOKEN.SLASH
            '(' -> token = TOKEN.LPAREN
            ')' -> token = TOKEN.RPAREN
            '{' -> token = TOKEN.LBRACE
            '}' -> token = TOKEN.RBRACE
            ',' -> token = TOKEN.COMMA
            '>' -> token = TOKEN.GT
            '<' -> token = TOKEN.LT
            CONST_EOF -> token = TOKEN.EOF
            else -> {
                if (isLetter(ch)) {
                    return getLetter()
                } else if (isDigit(ch)) {
                    return getNumber()
                } else {
                    TOKEN.ILLEGAL
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

    private fun getNumber(): TOKEN {
        val readNumber = readNumber()
        val token: TOKEN = TOKEN.INT
        token.value = readNumber
        return token
    }

    private fun getLetter(): TOKEN {
        val token: TOKEN
        val readLetter = readLetter()
        if (keywordsMap.containsKey(readLetter)) {
            token = keywordsMap[readLetter]!!
        } else {
            token = TOKEN.IDENT
            token.value = readLetter
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