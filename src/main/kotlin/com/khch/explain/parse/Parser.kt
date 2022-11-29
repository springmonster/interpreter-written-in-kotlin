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
    val precedence = mutableMapOf(
        Token.EQ to EQUALS,
        Token.NOT_EQ to EQUALS,
        Token.LT to LESS_GREATER,
        Token.GT to LESS_GREATER,
        Token.PLUS to SUM,
        Token.MINUS to SUM,
        Token.SLASH to PRODUCT,
        Token.ASTERISK to PRODUCT,
    )

    private fun peekPrecedence(): Int {
        return if (precedence.containsKey(peekToken.tokenType)) {
            precedence[peekToken.tokenType]!!
        } else {
            LOWEST
        }
    }

    private fun curPrecedence(): Int {
        return if (precedence.containsKey(curToken.tokenType)) {
            precedence[curToken.tokenType]!!
        } else {
            LOWEST
        }
    }

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

        // prefix
        registerPrefix(Token.IDENT, ::parseIdentifier)
        registerPrefix(Token.INT, ::parseIntLiteral)
        registerPrefix(Token.BANG, ::parsePrefixExpression)
        registerPrefix(Token.MINUS, ::parsePrefixExpression)
        registerPrefix(Token.TRUE, ::parseBoolean)
        registerPrefix(Token.FALSE, ::parseBoolean)
        registerPrefix(Token.LPAREN, ::parseGroupExpression)
        registerPrefix(Token.IF, ::parseIfExpression)

        // infix
        registerInfix(Token.PLUS, ::parseInfixExpression)
        registerInfix(Token.MINUS, ::parseInfixExpression)
        registerInfix(Token.SLASH, ::parseInfixExpression)
        registerInfix(Token.ASTERISK, ::parseInfixExpression)
        registerInfix(Token.EQ, ::parseInfixExpression)
        registerInfix(Token.NOT_EQ, ::parseInfixExpression)
        registerInfix(Token.LT, ::parseInfixExpression)
        registerInfix(Token.GT, ::parseInfixExpression)

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
        stmt.expression = parseExpression(LOWEST)

        if (peekTokenIs(Token.SEMICOLON)) {
            nextToken()
        }
        return stmt
    }

    private fun parseExpression(precedence: Int): Expression? {
        val isPrefixExist = prefixParseFnMap.containsKey(curToken.tokenType)
        return if (!isPrefixExist) {
            errors.add("no prefix parse function for ${curToken.tokenType} found")
            null
        } else {
            val prefixFn = prefixParseFnMap[curToken.tokenType]

            var leftExp = prefixFn?.invoke()!!

            while (!peekTokenIs(Token.SEMICOLON) && precedence < peekPrecedence()) {
                if (!infixParseFnMap.containsKey(peekToken.tokenType)) {
                    return leftExp
                }

                val infixFn = infixParseFnMap[peekToken.tokenType]

                nextToken()

                leftExp = infixFn?.invoke(leftExp)!!
            }

            leftExp
        }
    }

    private fun parseLetStatement(): LetStatement? {
        val letStatement = LetStatement(token = curToken)

        if (!expectPeek(Token.IDENT)) {
            return null
        }

        letStatement.name = Identifier(token = curToken, value = curToken.literal)

        if (!expectPeek(Token.ASSIGN)) {
            return null
        }

        while (!curTokenIs(Token.SEMICOLON)) {
            nextToken()
        }

        return letStatement
    }

    private fun parseReturnStatement(): ReturnStatement {
        val statement = ReturnStatement(token = curToken)

        nextToken()

        while (!curTokenIs(Token.SEMICOLON)) {
            nextToken()
        }

        return statement
    }

    private fun parseIntLiteral(): Expression? {
        val integerLiteral = IntegerLiteral(token = curToken)
        integerLiteral.value = Integer.valueOf(curToken.literal)
        return integerLiteral
    }

    private fun parsePrefixExpression(): Expression? {
        val prefixExpression = PrefixExpression(token = curToken)
        prefixExpression.operator = curToken.literal

        nextToken()

        prefixExpression.right = parseExpression(PREFIX)

        return prefixExpression
    }

    private fun parseInfixExpression(leftExp: Expression): Expression? {
        val infixExpression = InfixExpression(token = curToken, operator = curToken.literal, left = leftExp)

        // 获取当前优先级
        val precedence = curPrecedence()
        // 获取下一个字符
        nextToken()

        infixExpression.right = parseExpression(precedence)

        return infixExpression
    }

    private fun parseBoolean(): Expression? {
        val booleanExpression = BooleanExpression(token = curToken)
        booleanExpression.value = curTokenIs(Token.TRUE)
        return booleanExpression
    }

    private fun parseGroupExpression(): Expression? {
        nextToken()

        val parseExpression = parseExpression(LOWEST)

        if (!expectPeek(Token.RPAREN)) {
            return null
        }

        return parseExpression
    }

    private fun parseIfExpression(): Expression? {
        val ifExpression = IfExpression(token = curToken)

        if (!expectPeek(Token.LPAREN)) {
            return null
        }

        nextToken()

        ifExpression.condition = parseExpression(LOWEST)

        if (!expectPeek(Token.RPAREN)) {
            return null
        }

        if (!expectPeek(Token.LBRACE)) {
            return null
        }

        ifExpression.consequence = parseBlockStatement()

        if (peekTokenIs(Token.ELSE)) {
            nextToken()

            if (!expectPeek(Token.LBRACE)) {
                return null
            }

            ifExpression.alternative = parseBlockStatement()
        }

        return ifExpression
    }

    private fun parseBlockStatement(): BlockStatement? {
        val blockStatement = BlockStatement(token = curToken, statements = mutableListOf())

        nextToken()

        while (!curTokenIs(Token.RBRACE) && !curTokenIs(Token.EOF)) {
            val parseStatement = parseStatement()
            if (parseStatement != null) {
                blockStatement.statements.add(parseStatement)
            }
            nextToken()
        }

        return blockStatement
    }

    private fun curTokenIs(tokenType: String): Boolean {
        return curToken.tokenType == tokenType
    }

    private fun peekTokenIs(tokenType: String): Boolean {
        return peekToken.tokenType == tokenType
    }

    private fun expectPeek(tokenType: String): Boolean {
        return if (peekTokenIs(tokenType)) {
            nextToken()
            true
        } else {
            peekError(tokenType)
            false
        }
    }

    private fun parseIdentifier(): Expression {
        return Identifier(token = curToken, value = curToken.literal)
    }

    private fun peekError(tokenType: String) {
        errors.add("expected next token to be ${tokenType}, got ${peekToken.tokenType} instead")
    }
}

typealias prefixParseFn = () -> Expression?
typealias infixParseFn = (Expression) -> Expression?