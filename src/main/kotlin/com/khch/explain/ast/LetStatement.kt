package com.khch.explain.ast

import com.khch.explain.ast.interfaces.Expression
import com.khch.explain.ast.interfaces.Statement
import com.khch.explain.token.TOKEN

class LetStatement(private val token: TOKEN) : Statement {
    lateinit var name: Identifier
    lateinit var expression: Expression

    override fun statementNode() {
    }

    override fun tokenLiteral(): String {
        return token.literal
    }
}