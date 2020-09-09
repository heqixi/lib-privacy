package com.seewo.student.libutils.utils

import android.annotation.SuppressLint
import android.util.Log
import com.seewo.student.libutils.reflect.Reflect
import java.lang.reflect.Method

class SystemPropertyInternal {
    companion object {
        private const val TAG = "SystemPropertiesInternal"
        private const val SYSTEM_PROPERTIES = "android.os.SystemProperties"

        fun getBoolean(key: String, defaultValue: Boolean): Boolean {
            val value = get(key, defaultValue.toString())
            return if (value.isEmpty()) defaultValue else value.toBoolean()
        }

        @SuppressLint("PrivateApi")
        fun get(key: String, defaultValue: String = ""): String {
            var value = ""
            try {
                val c = Class.forName(SYSTEM_PROPERTIES)
                val get: Method = c.getMethod("get", String::class.java, String::class.java)
                value = get.invoke(c, key, defaultValue).toString()
            } catch (ignore: Exception) {
                Log.e(TAG, ignore.message)
            }
            return if (value.isEmpty()) defaultValue else value
        }

        fun set(key: String, value: String) {
            Reflect.on(SYSTEM_PROPERTIES).call("set", key, value)
        }
    }
}