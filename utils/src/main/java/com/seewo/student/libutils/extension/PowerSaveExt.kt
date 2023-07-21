package com.seewo.student.libutils.extension

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.seewo.student.libutils.utils.PowerSaveModeHelper

fun Context?.observePowerSaveModeActive(owner: LifecycleOwner, onChangedCallBack: (isPowerSaveModeActive: Boolean) -> Unit) {
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