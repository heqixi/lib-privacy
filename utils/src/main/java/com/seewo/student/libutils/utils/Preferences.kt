package com.seewo.student.libutils.utils

import android.annotation.SuppressLint
import android.content.Context

class Preferences(context: Context, name: String) {

    private val instance = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun setValue(key: String, value: Int) {
        instance.edit().putInt(key, value).apply()
    }

    fun setValue(key: String, value: String) {
        instance.edit().putString(key, value).apply()
    }

    fun setValue(key: String, value: Boolean) {
        instance.edit().putBoolean(key, value).apply()
    }

    fun getIntValue(key: String, defaultValue: Int = 0): Int {
        return instance.getInt(key, defaultValue)
    }

    fun getStringValue(key: String, defaultValue: String = ""): String {
        return instance.getString(key, defaultValue) ?: defaultValue
    }

    fun getBooleanValue(key: String, defaultValue: Boolean = false): Boolean {
        return instance.getBoolean(key, defaultValue)
    }

    fun removeValue(key: String) {
        instance.edit().remove(key).apply()
    }

    @SuppressLint("ApplySharedPref")
    fun clear() {
        instance.edit().clear().commit()
    }
}