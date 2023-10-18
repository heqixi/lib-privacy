package com.seewo.student.libutils.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.BatteryManager
import android.provider.Settings

/**
 * https://kb.cvte.com/pages/viewpage.action?pageId=368008665
 *
 * bit0：表示是否打开省电模式，打开为 1 ，关闭为 0，应用设置端根据 UI 的状态，进行设置
 * bit1：表示是否插电源，插适配器为 1，关闭为 0，监听充电的状态，进行设置
 * bit 2- bit5：为预留状态
 * bit6：表示是否打开省电措施，打开为1，关闭为0，注意于 bit0 区分是 bit6 表示实际功能，bit0 表示 UI 状态。
 * bit7：表示是否强制开启省电模式，开启为 1，关闭为0，如果这个值被设置，则不需要关注其他的 bit 的状态，强制打开所有的省电措施。编译的时候直接写死。
 *
 */
object PowerSaveModeHelper {
    // 0000 0001 是否省电模式开关打开
    private const val MODE_SWITCH_ON = 0x01

    // 0000 0010 是否已插入电源
    private const val MODE_POWER_PLUG_IN = 0x02

    // 0100 0000 是否省电模式激活
    private const val MODE_ACTIVE = 0x40

    // 1000 0000 是否省电模式强制激活
    private const val MODE_FORCE_ACTIVE = 0x80

    private const val KEY_POWER_SAVE_MODE = "global_ss_power_save_mode"
    private const val KEY_PROPERTY_SAVE_MODE = "persist.sys.seewo.powersave.mode"

    val settingsUriOfPowerSaveMode: Uri = Settings.Global.getUriFor(KEY_POWER_SAVE_MODE)

    // 处理 V1 机型 强制激活省电模式
    fun isPowerSaveForceActive(context: Context): Boolean {
        if (isForceInactivatedOfHistoryDevice()) {
            return false
        }
        if (SSDeviceUtils.isV1Series() || SSDeviceUtils.isT1Series()) {
            return true
        }

        val powerSaveMode = Settings.Global.getInt(context.contentResolver, KEY_POWER_SAVE_MODE, 0)
        return (powerSaveMode and MODE_FORCE_ACTIVE) != 0
    }

    /**
     * bit6 or bit7 为1时，表示省电模式为激活状态
     */
    fun isPowerSaveModeActive(context: Context): Boolean {
        if (isForceInactivatedOfHistoryDevice()) {
            return false
        }

        if (isPowerSaveForceActive(context)) {
            return true
        }

        val powerSaveMode = Settings.Global.getInt(context.contentResolver, KEY_POWER_SAVE_MODE, 0)
        return (powerSaveMode and MODE_ACTIVE) != 0 || (powerSaveMode and MODE_FORCE_ACTIVE) != 0
    }

    /**
     * bit1 为1时，表示省电模式开关为开
     */
    fun getPowerSaveModeSwitchOn(context: Context): Boolean {
        val powerSaveMode = Settings.Global.getInt(context.contentResolver, KEY_POWER_SAVE_MODE, 0)
        return (powerSaveMode and MODE_SWITCH_ON) != 0
    }

    fun setPowerSaveModeSwitchOn(context: Context, isSwitchOn: Boolean) {
        var powerSaveMode = Settings.Global.getInt(context.contentResolver, KEY_POWER_SAVE_MODE, 0)

        // 处理省电模式开关状态
        powerSaveMode = if (isSwitchOn) {
            powerSaveMode or MODE_SWITCH_ON
        } else {
            powerSaveMode and MODE_SWITCH_ON.inv()
        }

        // 处理省电模式当前充电状态
        powerSaveMode = if (isBatteryCharging(context)) {
            powerSaveMode or MODE_POWER_PLUG_IN
        } else {
            powerSaveMode and MODE_POWER_PLUG_IN.inv()
        }

        val isPowerSaveModeActive = when {
            // 强制激活省电模式
            (powerSaveMode and MODE_FORCE_ACTIVE) != 0 -> true
            // 省电模式打开且未充电
            (powerSaveMode and MODE_SWITCH_ON) != 0 && (powerSaveMode and MODE_POWER_PLUG_IN) == 0 -> true
            else -> false
        }

        // 处理省电模式激活状态
        powerSaveMode = if (isPowerSaveModeActive) {
            powerSaveMode or MODE_ACTIVE
        } else {
            powerSaveMode and MODE_ACTIVE.inv()
        }

        Settings.Global.putInt(context.contentResolver, KEY_POWER_SAVE_MODE, powerSaveMode)
        SystemPropertyInternal.set(KEY_PROPERTY_SAVE_MODE, powerSaveMode.toString())
    }

    fun isBatteryCharging(context: Context): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    fun isForceInactivatedOfHistoryDevice(): Boolean {
        return SSDeviceUtils.isW1() || SSDeviceUtils.isW2() || SSDeviceUtils.isW3Normal()
    }
}