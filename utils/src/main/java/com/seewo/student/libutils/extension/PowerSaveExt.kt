package com.seewo.student.libutils.extension

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.seewo.student.libutils.utils.PowerSaveModeHelper
import com.seewo.student.libutils.utils.SSDeviceUtils

fun Context?.observePowerSaveModeChange(owner: LifecycleOwner, onChangedCallBack: () -> Unit) {
    if (PowerSaveModeHelper.isForceInactivatedOfHistoryDevice()) {
        return
    }
    if (this == null || owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }

    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            onChangedCallBack.invoke()
        }
    }
    contentResolver.registerContentObserver(PowerSaveModeHelper.settingsUriOfPowerSaveMode, true, contentObserver)
    owner.lifecycle.addObserver(LifecycleInterObserver(this, owner, contentObserver))
}

fun Context?.observePowerSaveModeOpen(owner: LifecycleOwner, onChangedCallBack: (isPowerSaveModeOpen: Boolean) -> Unit) {
    if (PowerSaveModeHelper.isForceInactivatedOfHistoryDevice()) {
        return
    }
    if (this == null || owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }

    var isPowerSaveModeOpen = PowerSaveModeHelper.getPowerSaveModeSwitchOn(this)
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val isCurrentActive = PowerSaveModeHelper.getPowerSaveModeSwitchOn(this@observePowerSaveModeOpen)
            if (isPowerSaveModeOpen != isCurrentActive) {
                onChangedCallBack.invoke(isCurrentActive)
                isPowerSaveModeOpen = isCurrentActive
            }
        }
    }
    contentResolver.registerContentObserver(PowerSaveModeHelper.settingsUriOfPowerSaveMode, true, contentObserver)
    owner.lifecycle.addObserver(LifecycleInterObserver(this, owner, contentObserver))
}

fun Context?.observePowerSaveModeActive(owner: LifecycleOwner, onChangedCallBack: (isPowerSaveModeActive: Boolean) -> Unit) {
    if (PowerSaveModeHelper.isForceInactivatedOfHistoryDevice()) {
        return
    }
    if (this == null || owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }

    var isPowerSaveModeActive = PowerSaveModeHelper.isPowerSaveModeActive(this)
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val isCurrentActive = PowerSaveModeHelper.isPowerSaveModeActive(this@observePowerSaveModeActive)
            if (isPowerSaveModeActive != isCurrentActive) {
                onChangedCallBack.invoke(isCurrentActive)
                isPowerSaveModeActive = isCurrentActive
            }
        }
    }
    contentResolver.registerContentObserver(PowerSaveModeHelper.settingsUriOfPowerSaveMode, true, contentObserver)
    owner.lifecycle.addObserver(LifecycleInterObserver(this, owner, contentObserver))
}

fun Context?.observePowerSaveModeActiveForever(onChangedCallBack: (isPowerSaveModeActive: Boolean) -> Unit) {
    if (PowerSaveModeHelper.isForceInactivatedOfHistoryDevice()) {
        return
    }
    if (this == null) {
        return
    }
    var isPowerSaveModeActive = PowerSaveModeHelper.isPowerSaveModeActive(this)
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val isCurrentActive = PowerSaveModeHelper.isPowerSaveModeActive(this@observePowerSaveModeActiveForever)
            if (isPowerSaveModeActive != isCurrentActive) {
                onChangedCallBack.invoke(isCurrentActive)
                isPowerSaveModeActive = isCurrentActive
            }
        }
    }
    contentResolver.registerContentObserver(PowerSaveModeHelper.settingsUriOfPowerSaveMode, true, contentObserver)
}

internal class LifecycleInterObserver(
    val context: Context,
    private val owner: LifecycleOwner,
    private val contentObserver: ContentObserver
) : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
            context.contentResolver.unregisterContentObserver(contentObserver)
        }
    }
}