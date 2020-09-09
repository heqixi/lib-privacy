package com.seewo.student.libutils.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import android.view.Surface
import com.seewo.student.libutils.reflect.Reflect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*

object ScreenShotHelper {
    private const val TAG = "ScreenShotHelper"
    private const val FLAG_VISIBLE_ACTIVITY = "sys.visible.activity"
    private const val FLAG_VIDEO_ACTIVITY = "videoplayer"
    private const val FLAG_SHOT_FULLSCREEN = "persist.sys.shot_fullscreen"
    private const val MIN_LAYER = 0
    private const val MAX_LAYER = 31000

    /**
     * 0: 截取slave，可以支持全屏／内容，可以截取视频。不会截取到弹窗／工具栏。
     * 1: 安卓原生，可以支持分层截图，但不能截取视频。（OSD(即ANDROID））
     * 2: 截取master，可以截取视频，但截取的是整个屏幕，不支持分层截图，会截到弹窗／工具栏。（OSD+VIDEO）
     */
    private const val FLAG_SHOT_TYPE = "persist.sys.shot_type"

    /**
     *  delay(100): 系统同步相关配置
     */
    suspend fun getScreenShot(context: Context): Bitmap? = withContext(Dispatchers.IO) {
        enterScreenShotMode()
        delay(100)
        val displayMetrics = context.resources.displayMetrics
        val screenBitmap = invokeScreenshotP(displayMetrics.widthPixels, displayMetrics.heightPixels)
        SystemPropertyInternal.set(FLAG_SHOT_TYPE, "2")
        screenBitmap
    }

    private fun enterScreenShotMode() {
        SystemPropertyInternal.set(FLAG_SHOT_FULLSCREEN, "0")
        val visibleActivity = SystemPropertyInternal.get(FLAG_VISIBLE_ACTIVITY)
        if (visibleActivity.toLowerCase(Locale.getDefault()).contains(FLAG_VIDEO_ACTIVITY)) {
            // 有视频，截取ANDROID+VIDEO，视频混合会导致画质降低
            Log.d(TAG, "playing video.")
            SystemPropertyInternal.set(FLAG_SHOT_TYPE, "2")
        } else {
            // 无视频，截取ANDROID，画质更好
            Log.d(TAG, "not playing video.")
            SystemPropertyInternal.set(FLAG_SHOT_TYPE, "1")
        }
    }

    private fun invokeScreenshotP(width: Int, height: Int): Bitmap? {
        val screenRect = Rect(0, 0, 0, 0)
        return try {
            Reflect.on("android.view.SurfaceControl")
                .call(
                    "screenshot",
                    screenRect, width, height, MIN_LAYER, MAX_LAYER, false, Surface.ROTATION_0
                )
                .get() as Bitmap
        } catch (e: Exception) {
            Log.e("ScreenShotHelper", e.message)
            null
        }
    }
}