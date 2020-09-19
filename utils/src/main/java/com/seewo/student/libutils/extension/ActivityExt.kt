package com.seewo.student.libutils.extension

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup

val Activity.contentView: ViewGroup
    get() = findViewById(android.R.id.content)

fun Activity.getCurrentActivityCapture(): Bitmap {
    val view: View = window.decorView.findViewById(android.R.id.content)
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}