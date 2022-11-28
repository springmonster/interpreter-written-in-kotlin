package com.khch.explain.ast

interface Node {
    fun tokenLiteral(): String?

    fun string(): String?
}

interface Statement : Node {
    fun statementNode()
}

interface Expression:Node {
    fun expressionNode()
}