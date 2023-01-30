package com.seewo.student.libutils.utils

import android.os.Build

object SSDeviceUtils {

    fun isW0(): Boolean {
        return Build.MODEL.startsWith(prefix = "TC01", ignoreCase = true)
    }

    fun isW1(): Boolean {
        return Build.MODEL.startsWith(prefix = "TC02", ignoreCase = true)
    }

    fun isW2(): Boolean {
        return Build.MODEL.startsWith(prefix = "DT15PA", ignoreCase = true)
    }

    fun isW3(): Boolean {
        return Build.MODEL.startsWith(prefix = "XTW31", ignoreCase = true)
    }

    fun isV1(): Boolean {
        return Build.MODEL.startsWith(prefix = "XPV11A", ignoreCase = true)
    }
}