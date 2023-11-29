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

    @Deprecated("使用 isW3Series() 替换")
    fun isW3(): Boolean {
        return isW3Series()
    }

    /**
     * W3 系列，包含 W3、W3Pro
     */
    fun isW3Series(): Boolean {
        return Build.MODEL.startsWith(prefix = "XTW31", ignoreCase = true)
    }

    fun isW3Pro(): Boolean {
        return isW3Series() && isProSalasModel()
    }

    fun isW3Normal(): Boolean {
        return isW3Series() && !isProSalasModel()
    }

    @Deprecated("使用 isV1Series() 替换")
    fun isV1(): Boolean {
        return isV1Series()
    }

    /**
     * V1 系列，包含 V1、V1Pro
     */
    fun isV1Series(): Boolean {
        return Build.MODEL.startsWith(prefix = "XPV11", ignoreCase = true)
    }

    /**
     * V1 Pro 版
     */
    fun isV1Pro(): Boolean {
        return isV1Series() && isProSalasModel()
    }

    /**+
     * V1 标准版
     */
    fun isV1Normal(): Boolean {
        return isV1Series() && !isProSalasModel()
    }

    /**
     * T1 系列
     */
    fun isT1Series(): Boolean {
        return Build.MODEL.startsWith(prefix = "XPT11", ignoreCase = true)
    }

    /**
     * Ace1 系列，包含 Ace1、Ace1Pro
     */
    fun isAce1Series(): Boolean {
        return Build.MODEL.startsWith(prefix = "XTA11", ignoreCase = true)
    }

    fun isAce1Pro(): Boolean {
        return isAce1Series() && isProSalasModel()
    }

    fun isAce1Normal(): Boolean {
        return isAce1Series() && !isProSalasModel()
    }

    private fun isProSalasModel(): Boolean {
        return getSalesModel().equals("pro", ignoreCase = true)
    }

    fun getSalesModel(): String {
        var salesModel = SystemPropertyInternal.get("ro.seewo.sales.model", "").ifEmpty {
            SystemPropertyInternal.get("ro.seewo.software.type", "normal")
        }
        if (salesModel == "none") {
            salesModel = "normal"
        }
        return salesModel
    }

    fun getSystemTags(): String {
        return SystemPropertyInternal.get("ro.seewo.tags", "")
    }
}
