package com.khch.explain.ast

class Ast {
    val statements = mutableListOf<Statement>()

    fun tokenLiteral(): String {
        return if (statements.isNotEmpty()) {
            statements[0].tokenLiteral()
        } else {
            ""
        }
    }
}
