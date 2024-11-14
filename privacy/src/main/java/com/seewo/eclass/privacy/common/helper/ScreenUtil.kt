package com.seewo.eclass.privacy.common.helper

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager

internal object ScreenUtil {

    fun hideNavigationBar(activity: Activity) {
        hideNavigationBar(activity, Color.TRANSPARENT, true)
    }

    /**
     * 隐藏状态栏
     */
    private fun hideNavigationBar(activity: Activity, navigationBarColor: Int, showStatusBar: Boolean) {
        setFullScreen(activity, navigationBarColor, showStatusBar)
        val view = activity.window.decorView
        view.setOnSystemUiVisibilityChangeListener { setFullScreen(activity, navigationBarColor, showStatusBar) }
    }

    private fun setFullScreen(activity: Activity, color: Int, showStatusBar: Boolean) {
        val decorView = activity.window.decorView
        var option = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        if (!showStatusBar) {
            option = option or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        decorView.systemUiVisibility = option
        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.navigationBarColor = color
    }
}