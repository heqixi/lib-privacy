package com.seewo.eclass.privacy.common.ext

import android.content.res.Resources
import android.graphics.Outline
import android.os.SystemClock
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider

fun View.initPrivacyAlphaClick(supportEnableStyle: Boolean = false, clickFun: (View) -> Unit) {
    var touchDownTime = 0L
    setOnTouchListener { v, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownTime = SystemClock.elapsedRealtime()
                v.alpha = 0.5f
            }
            MotionEvent.ACTION_UP -> {
                if (SystemClock.elapsedRealtime() - touchDownTime < 500) {
                    v.performClick()
                }
                v.alpha = 1f
            }
            MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1f
            }
        }
        true
    }
    if (supportEnableStyle) {
        activeChangeOnEnableChange()
    }

    var lastClickTime = 0L
    setOnClickListener {
        val currentClickTime = SystemClock.uptimeMillis()
        if (currentClickTime - lastClickTime > 500) {
            lastClickTime = currentClickTime
            clickFun.invoke(this)
        }
    }
}

private fun View.activeChangeOnEnableChange() {
    var lastEnableState = isEnabled
    viewTreeObserver.addOnPreDrawListener {
        if (lastEnableState != isEnabled) {
            lastEnableState = isEnabled
            alpha = if (lastEnableState) 1f else 0.3f
        }
        true
    }
}

internal fun View.privacyRadius(radius: Float) {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    this.clipToOutline = true
}

internal val Float.privacy_dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

internal val Int.privacy_dp: Int
    get() = this.toFloat().privacy_dp.toInt()