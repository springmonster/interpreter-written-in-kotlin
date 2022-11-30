package com.khch.explain.ast

import com.khch.explain.token.Token

class Program : Node {
    val statements = mutableListOf<Statement>()

    override fun tokenLiteral(): String? {
        return if (statements.isNotEmpty()) {
            statements[0].tokenLiteral()
        } else {
            ""
        }
    }

    override fun string(): String {
        val sb = StringBuilder()
        statements.forEach {
            sb.append(it.string())
        }
        return sb.toString()
    }
}

// Identifier 也实现了 Expression 接口
class LetStatement(
    var token: Token? = null,
    var name: Identifier? = null,
    var value: Expression? = null
) : Statement, Node {
    override fun statementNode() {
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String {
        return token?.literal + " " + name?.string() + " = " + value?.string() + ";"
    }
}

class Identifier(var token: Token? = null, var value: String? = null) : Expression {
    override fun expressionNode() {
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        return value
    }
}

class ReturnStatement(var token: Token? = null, var returnValue: Expression? = null) : Statement,
    Node {
    override fun statementNode() {
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String {
        return token?.literal + " " + returnValue?.string() + ";"
    }
}

class ExpressionStatement(var token: Token? = null, var expression: Expression? = null) : Statement {
    override fun statementNode() {
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        return expression?.string()
    }
}

class IntegerLiteral(var token: Token? = null, var value: Int? = null) : Expression {
    override fun expressionNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        return token?.literal
    }
}

class PrefixExpression(var token: Token? = null, var operator: String? = null, var right: Expression? = null) :
    Expression {
    override fun expressionNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        return "(" + operator + right?.string() + ")"
    }
}

class InfixExpression(
    var token: Token? = null,
    var left: Expression? = null,
    var operator: String? = null,
    var right: Expression? = null
) :
    Expression {
    override fun expressionNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        return "(" + left?.string() + " " + operator + " " + right?.string() + ")"
    }
}

class BooleanExpression(var token: Token? = null, var value: Boolean? = null) : Expression {
    override fun expressionNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        return token?.literal
    }
}

class IfExpression(
    var token: Token? = null,
    var condition: Expression? = null,
    var consequence: BlockStatement? = null,
    var alternative: BlockStatement? = null
) : Expression {
    override fun expressionNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        var result = "if${condition?.string()} ${consequence?.string()}"

        if (alternative != null) {
            result += "else " + alternative?.string()
        }

        return result
    }
}

// token是{词法单元

class BlockStatement(
    var token: Token? = null,
    var statements: MutableList<Statement>
) : Statement {
    override fun statementNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        val sb = StringBuilder()
        statements.forEach {
            sb.append(it.string())
        }
        return sb.toString()
    }
}

class FunctionLiteral(
    var token: Token? = null,
    var parameters: MutableList<Identifier>,
    var body: BlockStatement? = null
) : Expression {
    override fun expressionNode() {
        TODO("Not yet implemented")
    }

    override fun tokenLiteral(): String? {
        return token?.literal
    }

    override fun string(): String? {
        val sb = StringBuilder()
        sb.append(tokenLiteral())
        sb.append("(")
        val joinToString = parameters.joinToString(separator = ", ") { it ->
            it.string().toString()
        }
        sb.append(joinToString)
        sb.append(")")
        sb.append(body?.string() ?: "")
        return sb.toString()
    }
}