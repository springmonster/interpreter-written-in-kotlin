package com.khch.explain.`object`

typealias ObjectType = String

object ObjectTypeStr {
    const val INTEGER_OBJ = "INTEGER"
    const val BOOLEAN_OBJ = "BOOLEAN"
    const val NULL_OBJ = "NULL"
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