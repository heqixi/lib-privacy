package com.seewo.student.libutils.extension

import android.app.Application
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, getDisplayMetrics())

val Int.dp: Int
    get() = this.toFloat().dp.toInt()

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, getDisplayMetrics())

val Int.sp: Int
    get() = this.toFloat().sp.toInt()

/**
 * 如果使用了 AutoSize 则使用 AutoSize 转化过的 resources，否则使用系统默认 resources
 */
fun getDisplayMetrics(): DisplayMetrics {
    var resources = Resources.getSystem()
    try {
        val clazz = Class.forName("me.jessyan.autosize.AutoSizeConfig")
        val getInstanceMethod = clazz.getMethod("getInstance")
        val instance = getInstanceMethod.invoke(null)
        val getApplicationMethod = clazz.getMethod("getApplication")
        val application = getApplicationMethod.invoke(instance) as Application
        resources = application.resources
    } catch (e: Exception) {
        // NOOP
    }
    return resources.displayMetrics
}
