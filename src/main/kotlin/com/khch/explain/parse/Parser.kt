package com.khch.explain.parse

import com.khch.explain.ast.*
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token
import com.khch.explain.token.TokenType

class Parser {
    companion object {
        const val LOWEST = 1

        // ==
        const val EQUALS = 2

        // > or <
        const val LESS_GREATER = 3

        // +
        const val SUM = 4

        // *
        const val PRODUCT = 5

        // -x or !x
        const val PREFIX = 6

        // myFunction(x)
        const val CALL = 7
    }

    var curToken: Token = Token()
    var peekToken: Token = Token()
    lateinit var lexer: Lexer
    val errors: MutableList<String> = mutableListOf()
    private val prefixParseFnMap = mutableMapOf<TokenType, prefixParseFn>()
    private val infixParseFnMap = mutableMapOf<TokenType, infixParseFn>()

    private fun registerPrefix(tokenType: TokenType, fn: prefixParseFn) {
        prefixParseFnMap[tokenType] = fn
    }

    private fun registerInfix(tokenType: TokenType, fn: infixParseFn) {
        infixParseFnMap[tokenType] = fn
    }

    fun new(lexer: Lexer): Parser {
        this.lexer = lexer

        nextToken()
        nextToken()

        registerPrefix(Token.IDENT, parseIdentifier())

        return this
    }

    private fun nextToken() {
        curToken = peekToken
        peekToken = this.lexer.nextToken()
    }

    fun parseProgram(): Program {
        val program = Program()

        while (curToken.tokenType != Token.EOF) {
            val parseStatement = parseStatement()
            if (parseStatement != null) {
                program.statements.add(parseStatement)
            }
            this.nextToken()
        }
        return program
    }

    private fun parseStatement(): Statement? {
        return when (this.curToken.tokenType) {
            Token.LET -> {
                return parseLetStatement()
            }

            Token.RETURN -> {
                return parseReturnStatement()
            }

            else -> {
                return parseExpressionStatement()
            }
        }
    }

    private fun parseExpressionStatement(): ExpressionStatement? {
        val stmt = ExpressionStatement(curToken)
        val parsedExpression = parseExpression(LOWEST)
        if (parsedExpression != null) {
            stmt.expression = parsedExpression
        } else {
            stmt.expression = Identifier(token = Token(), value = "")
        }

        while (nextTokenIs(Token.SEMICOLON)) {
            nextToken()
        }
        return stmt
    }

    private fun parseExpression(precedence: Int): Expression? {
        val isPrefixExist = prefixParseFnMap.containsKey(curToken.tokenType)
        return if (!isPrefixExist) {
            null
        } else {
            val prefixFn = prefixParseFnMap[curToken.tokenType]
            val leftExp = prefixFn!!()
            leftExp
        }
    }

    private fun parseLetStatement(): LetStatement? {
        val letStatement = LetStatement(curToken)

        if (!expectPeek(Token.IDENT)) {
            return null
        }

        letStatement.name = Identifier(token = curToken, value = curToken.literal)

        if (!expectPeek(Token.ASSIGN)) {
            return null
        }

        while (!currentTokenIs(Token.SEMICOLON)) {
            nextToken()
        }

        return letStatement
    }

    private fun parseReturnStatement(): ReturnStatement {
        val statement = ReturnStatement(curToken)

        nextToken()

        while (!currentTokenIs(Token.SEMICOLON)) {
            nextToken()
        }

        return statement
    }

    private fun currentTokenIs(tokenType: String): Boolean {
        return curToken.tokenType == tokenType
    }

    private fun nextTokenIs(tokenType: String): Boolean {
        return peekToken.tokenType == tokenType
    }

    private fun expectPeek(tokenType: String): Boolean {
        return if (nextTokenIs(tokenType)) {
            nextToken()
            true
        } else {
            peekError(tokenType)
            false
        }
    }

    private fun parseIdentifier(): () -> Expression = {
        Identifier(token = curToken, value = curToken.literal)
    }

    private fun peekError(tokenType: String) {
        errors.add("expected next token to be ${tokenType}, got ${peekToken.tokenType} instead")
    }
}

typealias prefixParseFn = () -> Expression?
typealias infixParseFn = (Expression) -> Expression?