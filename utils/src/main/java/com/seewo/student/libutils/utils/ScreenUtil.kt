package com.seewo.student.libutils.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.view.View
import android.view.WindowManager

class ScreenUtil {
    companion object {

        val screenWidth: Int
            get() = screenOutSize.x
        val screenHeight: Int
            get() = screenOutSize.y

        private val screenOutSize = Point()

        fun init(context: Context) {
            val windowService = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            windowService?.defaultDisplay?.getRealSize(screenOutSize)
        }

        fun getStatusBarHeight(): Int {
            // 不从 context 获取 resources，确保获取的 resources 没有被第三方多屏适配库修改
            val resources = Resources.getSystem()
            var statusBarHeight = 0
            val statusBarHeightResId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (statusBarHeightResId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(statusBarHeightResId)
            }
            return statusBarHeight
        }

        fun enterImmersive(activity: Activity) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            activity.window.statusBarColor = Color.TRANSPARENT
        }

        fun exitImmersive(activity: Activity) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            activity.window.statusBarColor = Color.TRANSPARENT
        }

        fun hideStatusBar(activity: Activity) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.TRANSPARENT
        }

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
}