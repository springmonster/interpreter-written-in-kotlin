package com.khch.explain.ast

import com.khch.explain.ast.interfaces.Expression
import com.khch.explain.token.TOKEN

class Identifier(private val token: TOKEN, val value: String) : Expression {
    override fun expressionNode() {
    }

    override fun tokenLiteral(): String {
        return token.literal
    }
}