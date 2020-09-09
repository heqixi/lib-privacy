package com.seewo.student.libutils.extension

import android.content.Context
import java.util.*

fun Context?.getSubjectDrawable(subjectId: String, extensionName: String, defaultDrawable: Int): Int {
    if (this == null) return defaultDrawable

    val drawableName = subjectId.toLowerCase(Locale.ENGLISH) + extensionName
    val resources = resources.getIdentifier(drawableName, "drawable", packageName)
    return if (resources == 0) defaultDrawable else resources
}