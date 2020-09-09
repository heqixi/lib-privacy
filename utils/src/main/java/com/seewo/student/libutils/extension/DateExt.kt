package com.seewo.student.libutils.extension

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH)

fun Long.isNewDay(): Boolean {
    return dateFormat.format(Date()) != dateFormat.format(Date(this))
}