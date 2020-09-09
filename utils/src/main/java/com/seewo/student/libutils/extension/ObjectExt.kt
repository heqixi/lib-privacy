package com.seewo.student.libutils.extension

fun Any?.nullRun(runnable: () -> Unit) {
    if (this == null) {
        runnable.invoke()
    }
}