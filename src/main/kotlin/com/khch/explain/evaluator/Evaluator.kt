package com.khch.explain.evaluator

import com.khch.explain.ast.*
import com.khch.explain.evaluator.Eval.FALSE
import com.khch.explain.evaluator.Eval.NULL
import com.khch.explain.evaluator.Eval.TRUE
import com.khch.explain.`object`.*
import com.khch.explain.`object`.ObjectTypeStr.ERROR_OBJ

object Eval {
    val TRUE = BooleanObj(true)
    val FALSE = BooleanObj(false)
    val NULL = NullObj()
}

fun isError(eval: Object?): Boolean {
    if (eval != null) {
        return eval.type() == ERROR_OBJ
    }
    return false
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
            val eval = eval(node.right)
            if (isError(eval)) {
                eval
            }
            evalPrefixExpression(node.operator, eval)
        }

        is InfixExpression -> {
            val left = eval(node.left)
            if (isError(left)) {
                left
            }

            val right = eval(node.right)
            if (isError(right)) {
                right
            }
            evalInfixExpression(node.operator, left, right)
        }

        is BlockStatement -> {
            evalBlockStatement(node.statements)
        }

        is IfExpression -> {
            evalIfExpression(node)
        }

        is ReturnStatement -> {
            val eval = eval(node.returnValue)
            if (isError(eval)) {
                eval
            }
            ReturnObj(value = eval)
        }

        else -> null
    }
}

fun evalBlockStatement(stmts: MutableList<Statement>): Object? {
    var result: Object? = null
    for (stmt in stmts) {
        result = eval(stmt)

        if (result != null && (result.type() == ObjectTypeStr.RETURN_OBJ ||
                    result.type() == ObjectTypeStr.NULL_OBJ)
        ) {
            return result
        }
    }
    return result
}

fun evalIfExpression(ifExpression: IfExpression): Object? {
    val condition = eval(ifExpression.condition)

    if (isError(condition)) {
        return condition
    }

    return if (isTruthy(condition)) {
        eval(ifExpression.consequence)
    } else if (ifExpression.alternative != null) {
        eval(ifExpression.alternative)
    } else {
        null
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
    if (left?.type() != right?.type()) {
        return newError("type mismatch: ${left?.type()} $operator ${right?.type()}")
    }
    if (left?.type() == ObjectTypeStr.INTEGER_OBJ && right?.type() == ObjectTypeStr.INTEGER_OBJ) {
        return evalIntegerInfixExpression(operator, left as IntegerObj, right as IntegerObj)
    }
    if (operator == "==") {
        return nativeBoolToBooleanObj(left == right)
    }
    if (operator == "!=") {
        return nativeBoolToBooleanObj(left != right)
    }
    return newError("unknown operator: ${left?.type()} $operator ${right?.type()}")
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
            return return newError("unknown operator: ${left?.type()} $operator ${right?.type()}")
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
        if (result is ErrorObj) {
            return result
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
        else -> {
            newError("unknown operator: $operator$right.type()")
        }
    }
}

fun evalMinusOperatorExpression(right: Object?): Object? {
    if (right?.type() != ObjectTypeStr.INTEGER_OBJ) {
        return newError("unknown operator: -${right?.type()}")
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

fun newError(msg: String): ErrorObj {
    return ErrorObj(message = msg)
}