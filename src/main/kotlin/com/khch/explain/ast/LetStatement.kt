package com.khch.explain.ast

import com.khch.explain.token.TOKEN

class LetStatement(private val token: TOKEN) : Statement, Expression {
    lateinit var expression: Expression

    override fun expressionNode() {
    }

    override fun statementNode() {
    }

    override fun tokenLiteral(): String {
        return token.name
    }
}