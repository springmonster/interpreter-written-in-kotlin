package com.khch.explain.parse

import com.khch.explain.ast.Ast
import com.khch.explain.ast.Identifier
import com.khch.explain.ast.LetStatement
import com.khch.explain.ast.ReturnStatement
import com.khch.explain.ast.interfaces.Statement
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token

class Parser {
    var currentToken: Token = Token()
    var nextToken: Token = Token()
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

    fun parseProgram(): Ast {
        while (currentToken.tokenType != Token.EOF) {
            val parseStatement = parseStatement()
            if (parseStatement != null) {
                ast.statements.add(parseStatement)
            }
            nextToken()
        }
        return ast
    }

    private fun parseStatement(): Statement? {
        return when (currentToken.tokenType) {
            Token.LET -> {
                return parseLetStatement() as Statement
            }

            Token.RETURN -> {
                return parseReturnStatement() as Statement
            }

            else -> null
        }
    }

    private fun parseLetStatement(): LetStatement? {
        val letStatement = LetStatement(currentToken)

        if (!expectNextTokenIs(Token.IDENT)) {
            return null
        }

        letStatement.name = Identifier(currentToken.literal, currentToken)

        if (!expectNextTokenIs(Token.ASSIGN)) {
            return null
        }

        while (!currentTokenIs(Token.SEMICOLON)) {
            nextToken()
        }

        return letStatement
    }

    private fun parseReturnStatement(): ReturnStatement? {
        val statement = ReturnStatement(currentToken)

        nextToken()

        while (!currentTokenIs(Token.SEMICOLON)) {
            nextToken()
        }

        return statement
    }

    private fun currentTokenIs(tokenType: String): Boolean {
        return currentToken.tokenType == tokenType
    }

    private fun nextTokenIs(tokenType: String): Boolean {
        return nextToken.tokenType == tokenType
    }

    private fun expectNextTokenIs(tokenType: String): Boolean {
        return if (nextTokenIs(tokenType)) {
            nextToken()
            true
        } else {
            println("expect token error!")
            false
        }
    }
}