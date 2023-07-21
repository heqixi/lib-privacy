package com.seewo.student.libutils.extension

import android.content.Context
import android.database.ContentObserver
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.seewo.student.libutils.utils.PowerSaveModeHelper

fun Context?.observePowerSaveModeChange(lifecycleOwner: LifecycleOwner, contentObserver: ContentObserver) {
    if (this == null) {
        Log.e("PowerSave", "context is null, observePowerSaveModeChange fail!")
        return
    }

    contentResolver.registerContentObserver(PowerSaveModeHelper.settingsUriOfPowerSaveMode, true, contentObserver)
    lifecycleOwner.lifecycle.addObserver(LifecycleInterObserver(this, contentObserver))
}


internal class LifecycleInterObserver(val context: Context, private val contentObserver: ContentObserver) : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            context.contentResolver.unregisterContentObserver(contentObserver)
        }
    }
}