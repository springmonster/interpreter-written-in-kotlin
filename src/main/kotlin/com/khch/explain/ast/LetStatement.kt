package com.khch.explain.ast

import com.khch.explain.ast.interfaces.Expression
import com.khch.explain.ast.interfaces.Node
import com.khch.explain.ast.interfaces.Statement
import com.khch.explain.token.Token

class LetStatement(private val token: Token) : Statement, Node {
    var name: Identifier? = null

    lateinit var value: Expression

    override fun statementNode() {
    }

    override fun tokenLiteral(): String {
        return token.literal
    }

    override fun string(): String {
        return token.literal + " " + name?.string() + " = "+ value.string() + ";"
    }
}