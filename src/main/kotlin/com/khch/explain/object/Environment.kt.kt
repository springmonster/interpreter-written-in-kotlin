package com.khch.explain.`object`

class Environment(var store: MutableMap<String, Object>? = null, var outer: Environment? = null) {

    fun newEnclosedEnvironment(outer: Environment): Environment {
        val e = Environment().newEnvironment()
        e.outer = outer
        return e
    }

    fun newEnvironment(): Environment {
        return Environment(mutableMapOf(), outer = null)
    }

    fun get(name: String): Object? {
        if (!store!!.contains(name) && outer != null) {
            return outer!!.get(name)
        }
        return store!![name]
    }

    fun set(name: String, value: Object): Object {
        store!![name] = value
        return value
    }
}