package com.seewo.eclass.privacy.common.ext

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.seewo.eclass.privacy.R

fun Context.showToast(pointRes: Int) {
    val toast = Toast(this).apply {
        setGravity(Gravity.BOTTOM, 0, 118.privacy_dp)
    }
    toast.view = createTextView(getString(pointRes))
    toast.show()
}

private fun Context.createTextView(pointRes: String): TextView {
    val textView = TextView(this)
    textView.setBackgroundResource(R.drawable.privacy_toast_common_bg)
    textView.textSize = 14F
    textView.includeFontPadding = false
    textView.setTextColor(Color.WHITE)
    textView.text = pointRes
    textView.gravity = Gravity.CENTER
    textView.setPadding(24.privacy_dp, 16.privacy_dp, 24.privacy_dp, 16.privacy_dp)
    return textView
}