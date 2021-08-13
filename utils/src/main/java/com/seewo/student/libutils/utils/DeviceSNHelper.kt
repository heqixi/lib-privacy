package com.seewo.student.libutils.utils

import android.content.Context
import android.os.Build
import android.provider.Settings

object DeviceSNHelper {
    /**
     * 获取板卡SN，烧录在板卡上，一般情况下不会发生变更。
     * 应用场景：跟随设备绑定，比如 应用激活码跟设备一一绑定
     */
    fun getDeviceBoardSN() = SystemPropertyInternal.get("ro.serialno", Build.ID)

    /**
     * 获取整机SN，烧录在系统中，一般在商返时会发生变更
     * 应用场景：跟随用户账号绑定，比如 设备管理员跟设备绑定，当该设备商返后，用户拿到这台设备后，无任务绑定信息
     */
    fun getMachineSN(context: Context) = Settings.System.getString(context.contentResolver, "machine_serial_number") ?: ""

    /**
     * 【兼容情况下使用：本应该使用 MachineSN ，但是之前使用 DeviceBoardSN】
     * https://kb.cvte.com/pages/viewpage.action?pageId=252395355
     *
     * 学习机V2.0.2版本中，添加一个新的属性 sys.seewo.machinesn
     * 通过 items 确保软件变更不会对之前的用户有影响
     * 应用判断如果新增属性值为 true ，则读取 整机SN，反之则使用 板卡SN
     */
    fun getCompatibleMachineSN(context: Context): String {
        val isMachineSNSupport = SystemPropertyInternal.getBoolean("sys.seewo.machinesn", false)
        return if (isMachineSNSupport) {
            getMachineSN(context)
        } else {
            getDeviceBoardSN()
        }
    }
}