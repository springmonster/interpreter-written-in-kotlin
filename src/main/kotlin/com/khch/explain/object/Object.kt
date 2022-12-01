package com.khch.explain.`object`

import com.khch.explain.ast.BlockStatement
import com.khch.explain.ast.Identifier

typealias ObjectType = String

object ObjectTypeStr {
    const val INTEGER_OBJ = "INTEGER"
    const val BOOLEAN_OBJ = "BOOLEAN"
    const val NULL_OBJ = "NULL"
    const val RETURN_OBJ = "RETURN"
    const val ERROR_OBJ = "ERROR"
    const val FUNCTION_OBJ = "FUNCTION"
}

interface Object {
    // 类型
    fun type(): ObjectType

    // 检查
    fun inspect(): String
}

class IntegerObj(val value: Int?) : Object {
    override fun type(): ObjectType {
        return ObjectTypeStr.INTEGER_OBJ
    }

    override fun inspect(): String {
        return value.toString()
    }
}

class BooleanObj(val value: Boolean?) : Object {
    override fun type(): ObjectType {
        return ObjectTypeStr.BOOLEAN_OBJ
    }

    override fun inspect(): String {
        return value.toString()
    }
}

class NullObj : Object {
    override fun type(): ObjectType {
        return ObjectTypeStr.NULL_OBJ
    }

    override fun inspect(): String {
        return "null"
    }
}

class ReturnObj(var value: Object? = null) : Object {
    override fun type(): ObjectType {
        return ObjectTypeStr.RETURN_OBJ
    }

    override fun inspect(): String {
        return value?.inspect() ?: ""
    }
}

class ErrorObj(var message: String? = null) : Object {
    override fun type(): ObjectType {
        return ObjectTypeStr.ERROR_OBJ
    }

    override fun inspect(): String {
        return ("ERROR: $message")
    }
}

class FunctionObj(
    var parameters: MutableList<Identifier>,
    var body: BlockStatement,
    var env: Environment
) : Object {
    override fun type(): ObjectType {
        return ObjectTypeStr.FUNCTION_OBJ
    }

    override fun inspect(): String {
        val sb = StringBuilder()
        sb.append("fn")
        sb.append("(")
        val joinToString = parameters.joinToString(separator = ", ") { it ->
            it.string().toString()
        }
        sb.append(joinToString)
        sb.append(")")
        sb.append("{")
        sb.append(body?.string() ?: "")
        sb.append("}")
        return sb.toString()
    }
}