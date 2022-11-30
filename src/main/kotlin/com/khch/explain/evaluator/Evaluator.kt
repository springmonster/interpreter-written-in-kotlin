package com.khch.explain.evaluator

import com.khch.explain.ast.*
import com.khch.explain.evaluator.Eval.FALSE
import com.khch.explain.evaluator.Eval.NULL
import com.khch.explain.evaluator.Eval.TRUE
import com.khch.explain.`object`.*

object Eval {
    val TRUE = BooleanObj(true)
    val FALSE = BooleanObj(false)
    val NULL = NullObj()
}

fun eval(node: Node?): Object? {
    return when (node) {
        is Program -> {
            evalStatements(node.statements)
        }

        is ExpressionStatement -> {
            eval(node.expression)
        }

        is IntegerLiteral -> {
            IntegerObj(value = node.value)
        }

        is BooleanExpression -> {
            nativeBoolToBooleanObj(node.value!!)
        }

        is PrefixExpression -> {
            evalPrefixExpression(node.operator, eval(node.right))
        }

        else -> null
    }
}

fun evalStatements(stmts: MutableList<Statement>): Object? {
    var result: Object? = null
    for (stmt in stmts) {
        result = eval(stmt)
    }
    return result
}

fun nativeBoolToBooleanObj(input: Boolean): BooleanObj {
    return if (input) TRUE else FALSE
}

fun evalPrefixExpression(operator: String?, right: Object?): Object? {
    return when (operator) {
        "!" -> evalBangOperatorExpression(right)
        "-" -> evalMinusOperatorExpression(right)
        else -> null
    }
}

fun evalMinusOperatorExpression(right: Object?): Object? {
    if (right?.type() != ObjectTypeStr.INTEGER_OBJ) {
        return null
    }
    val integerObj = right as IntegerObj
    return IntegerObj(value = -(integerObj.value)!!)
}

fun evalBangOperatorExpression(right: Object?): Object? {
    return when (right) {
        TRUE -> FALSE
        FALSE -> TRUE
        NULL -> TRUE
        else -> FALSE
    }
}