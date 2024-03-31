package com.hjy.wanandroid.filter

import android.content.SharedPreferences

fun SharedPreferences.Editor.put(key: String, value: Any?): Boolean {
    when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Float -> putFloat(key, value)
        is Boolean -> putBoolean(key, value)
        is Set<*> -> putStringSet(key, value as Set<String>)
        else -> throw IllegalArgumentException("Unsupported value type")
    }
    return commit()
}

fun SharedPreferences.get(key: String, default: Any?): Any? {
    return when (default) {
        is String -> getString(key, default)
        is Int -> getInt(key, default)
        is Long -> getLong(key, default)
        is Float -> getFloat(key, default)
        is Boolean -> getBoolean(key, default)
        is Set<*> -> getStringSet(key, default as Set<String>)
        else -> throw IllegalArgumentException("Unsupported default type")
    }
}