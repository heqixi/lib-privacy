package com.seewo.student.libutils.extension

import android.os.Handler

fun Handler.runInIdleHandler(function: () -> Unit) {
    looper.queue.addIdleHandler {
        function.invoke()
        false
    }
}