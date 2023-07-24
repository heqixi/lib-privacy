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

    fun isW3Pro(): Boolean {
        return isW3() && isProSalasModel()
    }

    fun isV1(): Boolean {
        return Build.MODEL.startsWith(prefix = "XPV11", ignoreCase = true)
    }

    fun isV1Pro(): Boolean {
        return isV1() && isProSalasModel()
    }

    private fun isProSalasModel(): Boolean {
        return SystemPropertyInternal.get("ro.seewo.sales.model", "").equals("pro", ignoreCase = true)
    }
}
