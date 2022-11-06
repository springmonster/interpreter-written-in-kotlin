package com.khch.explain.parse

import com.khch.explain.ast.Ast
import com.khch.explain.ast.LetStatement
import com.khch.explain.ast.Statement
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.TOKEN

class Parser {
    private var currentToken: TOKEN = TOKEN.EOF
    private var nextToken: TOKEN = TOKEN.EOF
    lateinit var lexer: Lexer
    var ast = Ast()

    fun new(lexer: Lexer): Parser {
        this.lexer = lexer
        nextToken()
        nextToken()
        return this
    }

    private fun nextToken() {
        currentToken = nextToken
        nextToken = this.lexer.nextToken()
    }

    fun parseProgram(): Ast? {
        while (currentToken != TOKEN.EOF) {
            val parseStatement = parseStatement()
            if (parseStatement != null) {
                ast.statements.add(parseStatement)
            }
            nextToken()
        }
        return ast
    }

    private fun parseStatement(): Statement? {
        return when (currentToken) {
            TOKEN.LET -> {
                return parseLetStatement() as Statement
            }

            else -> null
        }
    }

    private fun parseLetStatement(): LetStatement? {
        val letStatement = LetStatement(currentToken)

        if (!expectNextTokenIs(TOKEN.IDENT)) {
            return null
        }

        if (!expectNextTokenIs(TOKEN.ASSIGN)) {
            return null
        }

        while (!currentTokenIs(TOKEN.SEMICOLON)) {
            nextToken()
        }

        return letStatement
    }

    private fun currentTokenIs(token: TOKEN): Boolean {
        return currentToken == token
    }

    private fun nextTokenIs(token: TOKEN): Boolean {
        return nextToken == token
    }

    private fun expectNextTokenIs(token: TOKEN): Boolean {
        return if (nextTokenIs(token)) {
            nextToken()
            true
        } else {
            println("expect token error!")
            false
        }
    }
}