package com.seewo.student.libutils.extension

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.*
import android.os.SystemClock
import android.view.*
import android.view.animation.LinearInterpolator

private const val DEFAULT_DEBOUNCE_TIME = 500L

fun View.initAlphaClick(clickFun: (View) -> Unit = {}) {
    var touchDownTime = 0L
    setOnTouchListener { v, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownTime = SystemClock.elapsedRealtime()
                this.alpha = 0.5F
            }
            MotionEvent.ACTION_UP -> {
                if (SystemClock.elapsedRealtime() - touchDownTime < 500) {
                    v.post { v.performClick() }
                }
                v.alpha = 1F
            }
            MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1F
            }
        }
        true
    }
    initClickListener(clickFun)
}

fun View.initTouchListener(clickFun: (View) -> Unit) {
    var touchDownTime = 0L
    setOnTouchListener { v, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownTime = SystemClock.elapsedRealtime()
                v.animate()
                    .scaleX(0.90F)
                    .scaleY(0.90F)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(100)
                    .start()
            }
            MotionEvent.ACTION_UP -> {
                if (SystemClock.elapsedRealtime() - touchDownTime < 500) {
                    v.performClick()
                }
                v.animate().cancel()
                v.scaleX = 1F
                v.scaleY = 1F
            }
            MotionEvent.ACTION_CANCEL -> {
                v.animate().cancel()
                v.scaleX = 1F
                v.scaleY = 1F
            }
        }
        true
    }
    initClickListener(clickFun)
}

fun View.setClickZoomAnimation(scaleX: Float = 0.96F, scaleY: Float = 0.96F, duration: Long = 100) {
    setOnTouchListener(object : View.OnTouchListener {

        private val animateInterpolator = LinearInterpolator()

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            if (view == null) {
                return false
            }
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate()
                        .scaleX(scaleX)
                        .scaleY(scaleY)
                        .setInterpolator(animateInterpolator)
                        .setDuration(duration)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    view.animate().cancel()
                    view.scaleX = 1.0F
                    view.scaleY = 1.0F
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate().cancel()
                    view.scaleX = 1.0F
                    view.scaleY = 1.0F
                }
                else -> {
                    // NOOP
                }
            }
            return false
        }
    })
}

fun View.initClickListener(clickFun: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentClickTime = SystemClock.uptimeMillis()
        if (currentClickTime - lastClickTime > 500) {
            lastClickTime = currentClickTime
            clickFun.invoke(this)
        }
    }
}

fun View.radius(radius: Float) {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    this.clipToOutline = true
}

fun View.expandTouchArea(size: Float) {
    expandTouchArea(size.toInt())
}

fun View.expandTouchArea(size: Int) {
    val parentView = this.parent as ViewGroup
    parentView.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= size
        rect.bottom += size
        rect.left -= size
        rect.right += size

        parentView.touchDelegate = TouchDelegate(rect, this)
    }
}

fun View.getActivity(): Activity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.getViewCapture(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888).apply {
        density = resources.displayMetrics.densityDpi
    }
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

fun View.enterVisible() {
    this.visibility = View.VISIBLE
}

fun View.enterInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.enterGone() {
    this.visibility = View.GONE
}

fun View.setOnDebounceClickListener(
    debounceTime: Long = DEFAULT_DEBOUNCE_TIME,
    onDebounceClickListener: ((view: View) -> Unit)
) {
    var debounce = 0L
    setOnClickListener {
        if (debounce > SystemClock.elapsedRealtime()) {
            return@setOnClickListener
        } else {
            debounce = SystemClock.elapsedRealtime() + debounceTime
            onDebounceClickListener.invoke(it)
        }
    }
}

fun View.centerInScreen(locationArray: IntArray = IntArray(2)): Point {
    this.getLocationOnScreen(locationArray)
    return Point(locationArray[0] + this.width / 2, locationArray[1] + this.height / 2)
}