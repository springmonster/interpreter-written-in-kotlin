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

    fun readChar() {
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
            '=' -> token = TOKEN.EQUALS
            ';' -> token = TOKEN.SEMICOLON
            '+' -> token = TOKEN.ADD
            '-' -> token = TOKEN.MINUS
            '*' -> token = TOKEN.ASTERISK
            '/' -> token = TOKEN.SLASH
            '(' -> token = TOKEN.LPAREN
            ')' -> token = TOKEN.RPAREN
            '{' -> token = TOKEN.LBRACE
            '}' -> token = TOKEN.RBRACE
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

    private fun getNumber(): TOKEN {
        val readNumber = readNumber()
        val token: TOKEN = TOKEN.IDENT
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

    fun eatWhitespace() {
        if (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t') {
            readChar()
        }
    }

    fun isLetter(ch: Char): Boolean {
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_'
    }

    fun isDigit(ch: Char): Boolean {
        return '0' <= ch && ch <= '9'
    }
}