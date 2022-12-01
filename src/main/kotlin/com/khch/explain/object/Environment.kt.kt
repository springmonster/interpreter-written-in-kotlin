package com.khch.explain.`object`

class Environment(var store: MutableMap<String, Object>? = null) {
    fun newEnvironment(): Environment {
        return Environment(mutableMapOf())
    }

    fun get(name: String): Object? {
        return store!![name]
    }

    fun set(name: String, value: Object): Object {
        store!![name] = value
        return value
    }
}