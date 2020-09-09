package com.seewo.student.libutils.utils

import android.os.Handler
import android.os.Looper

class RepeatX {
    private var triggerTime = 0L
    private var throwFirst = 0L
    private var throwLast = 0L

    private val repeatEventHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    fun throwFirst(time: Long) = apply {
        this.throwFirst = time
    }

    fun throwLast(time: Long) = apply {
        this.throwLast = time
    }

    fun offerEvent(funEvent: () -> Unit) {
        when {
            throwFirst > 0L -> {
                val currentTimeMillis = System.currentTimeMillis()
                if (currentTimeMillis - triggerTime >= throwFirst) {
                    funEvent.invoke()
                    triggerTime = currentTimeMillis
                }
            }
            throwLast > 0L -> {
                repeatEventHandler.removeCallbacksAndMessages(null)
                repeatEventHandler.postDelayed({
                    funEvent.invoke()
                }, throwLast)
            }
            else -> funEvent.invoke()
        }
    }
}