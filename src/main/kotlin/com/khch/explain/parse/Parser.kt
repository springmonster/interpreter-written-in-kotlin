package com.khch.explain.parse

import com.khch.explain.ast.*
import com.khch.explain.ast.interfaces.Expression
import com.khch.explain.ast.interfaces.Statement
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token

class Parser {
    companion object {
        const val LOWEST = 1
        const val EQUALS = 2
        const val LESSGREATER = 3
        const val SUM = 4
        const val PRODUCT = 5
        const val PREFIX = 6
        const val CALL = 7
    }

    var currentToken: Token = Token()
    var nextToken: Token = Token()
    lateinit var lexer: Lexer
    private val prefixParseFnMap = mutableMapOf<String, Expression>()
    private val infixParseFnMap = mutableMapOf<String, Expression>()
    var ast = Ast()

    private fun registerPrefix(tokenType: String, prefixParseFn: Expression) {
        prefixParseFnMap[tokenType] = prefixParseFn
    }

    private fun registerInfix(tokenType: String, infixParseFn: Expression) {
        infixParseFnMap[tokenType] = infixParseFn
    }

    fun new(lexer: Lexer): Parser {
        this.lexer = lexer
        nextToken()
        nextToken()

        registerPrefix(Token.IDENT, parseIdentifier())

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

            else -> {
                return parseExpressionStatement() as Statement
            }
        }
    }

    private fun parseExpressionStatement(): ExpressionStatement? {
        val expression = ExpressionStatement(currentToken)
        val parsedExpression = parseExpression(LOWEST)
        if (parsedExpression != null) {
            expression.expression = parsedExpression
        } else {
            expression.expression = Identifier("", Token())
        }

        while (nextTokenIs(Token.SEMICOLON)) {
            nextToken()
        }
        return expression
    }

    private fun parseExpression(expression: Int): Expression? {
        val isPrefixExist = prefixParseFnMap.containsKey(currentToken.tokenType)
        return if (!isPrefixExist) {
            null
        } else {
            parseIdentifier()
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

    private fun parseIdentifier(): Expression {
        return Identifier(currentToken.literal, currentToken)
    }
}