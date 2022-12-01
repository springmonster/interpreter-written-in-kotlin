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
            evalProgram(node.statements)
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

        is InfixExpression -> {
            evalInfixExpression(node.operator, eval(node.left), eval(node.right))
        }

        is BlockStatement -> {
            evalBlockStatement(node.statements)
        }

        is IfExpression -> {
            evalIfExpression(node)
        }

        is ReturnStatement -> {
            ReturnObj(value = eval(node.returnValue))
        }

        else -> null
    }
}

fun evalBlockStatement(stmts: MutableList<Statement>): Object? {
    var result: Object? = null
    for (stmt in stmts) {
        result = eval(stmt)

        if (result != null && result.type() == ObjectTypeStr.RETURN_OBJ) {
            return result
        }
    }
    return result
}

fun evalIfExpression(ifExpression: IfExpression): Object? {
    val condition = eval(ifExpression.condition)

    if (isTruthy(condition)) {
        return eval(ifExpression.consequence)
    } else if (ifExpression.alternative != null) {
        return eval(ifExpression.alternative)
    } else {
        return null
    }
}

fun isTruthy(obj: Object?): Boolean {
    when (obj) {
        TRUE -> {
            return true
        }

        FALSE -> {
            return true
        }

        NULL -> {
            return false
        }

        else -> {
            return true
        }
    }
}

fun evalInfixExpression(operator: String?, left: Object?, right: Object?): Object? {
    if (left?.type() == ObjectTypeStr.INTEGER_OBJ && right?.type() == ObjectTypeStr.INTEGER_OBJ) {
        return evalIntegerInfixExpression(operator, left as IntegerObj, right as IntegerObj)
    }
    if (operator == "==") {
        return nativeBoolToBooleanObj(left == right)
    }
    if (operator == "!=") {
        return nativeBoolToBooleanObj(left != right)
    }
    return null
}

fun evalIntegerInfixExpression(operator: String?, left: IntegerObj?, right: IntegerObj?): Object? {
    val leftValue = left?.value ?: 0
    val rightValue = right?.value ?: 0
    when (operator) {
        "+" -> {
            return IntegerObj(value = (leftValue + rightValue))
        }

        "-" -> {
            return IntegerObj(value = (leftValue - rightValue))
        }

        "*" -> {
            return IntegerObj(value = (leftValue * rightValue))
        }

        "/" -> {
            return IntegerObj(value = (leftValue / rightValue))
        }

        ">" -> {
            return BooleanObj(value = (leftValue > rightValue))
        }

        "<" -> {
            return BooleanObj(value = (leftValue < rightValue))
        }

        "==" -> {
            return BooleanObj(value = (leftValue == rightValue))
        }

        "!=" -> {
            return BooleanObj(value = (leftValue != rightValue))
        }

        else -> {
            return null
        }
    }
}

fun evalProgram(stmts: MutableList<Statement>): Object? {
    var result: Object? = null
    for (stmt in stmts) {
        result = eval(stmt)

        if (result is ReturnObj) {
            return result.value
        }
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