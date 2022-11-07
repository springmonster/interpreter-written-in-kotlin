package com.khch.explain.ast

import com.khch.explain.ast.interfaces.Expression
import com.khch.explain.ast.interfaces.Node
import com.khch.explain.ast.interfaces.Statement
import com.khch.explain.token.Token

class ExpressionStatement(private val token: Token) : Statement {
    lateinit var expression: Expression

    override fun statementNode() {
    }

    override fun tokenLiteral(): String {
        return token.literal
    }

    override fun string(): String {
        return "expression"
    }
}