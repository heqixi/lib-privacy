package com.seewo.eclass.privacy.common.helper

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log

internal object SystemHelper {
    private const val TAG = "SystemHelper"
    private val displayMetrics = intArrayOf(
        DisplayMetrics.DENSITY_XXHIGH,
        DisplayMetrics.DENSITY_XHIGH,
        DisplayMetrics.DENSITY_HIGH,
        DisplayMetrics.DENSITY_TV
    )

    fun getAppName(context: Context?, packageName: String?): String? {
        if (context == null || packageName.isNullOrEmpty()) return null
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
            applicationInfo.loadLabel(context.packageManager).toString()
        } catch (e: Exception) {
            Log.e(TAG, "getAppName $packageName error")
            null
        }
    }

    fun getAppIcon(context: Context?, pkgName: String): Drawable? {
        if (context == null) return null
        try {
            val pi: PackageInfo = context.packageManager.getPackageInfo(pkgName, 0)
            val otherAppCtx: Context = context.createPackageContext(pkgName, Context.CONTEXT_IGNORE_SECURITY)
            for (displayMetric in displayMetrics) {
                try {
                    val options = BitmapFactory.Options().apply {
                        this.inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeResource(otherAppCtx.resources, pi.applicationInfo.icon, options)
                    // 处理理图标的OOM问题，极端情况下，某个APP出现 4900 * 4900 的Icon。 针对多数的图标显示场景，256的像素已经足够，且不会导致内存问题。
                    if (options.outHeight <= 256 && options.outWidth <= 256) {
                        val d = otherAppCtx.resources.getDrawableForDensity(pi.applicationInfo.icon, displayMetric, null)
                        if (d != null) {
                            return d
                        }
                    } else {
                        continue
                    }
                } catch (e: Exception) {
                    continue
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAppIcon: Name not found $pkgName")
        }
        return getNormalAppIcon(context, pkgName)
    }

    fun Context.hasFileInAsserts(filename: String, directory: String): Boolean {
        return try {
            resources?.assets?.list(directory)?.forEach {
                if (filename == it) return true
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getNormalAppIcon(context: Context, pkgName: String): Drawable? {
        return try {
            val packageManager: PackageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(pkgName, 0)
            applicationInfo.loadIcon(packageManager)
        } catch (e: Exception) {
            Log.e(TAG, "getAppIcon: $pkgName is not installed")
            null
        }
    }
}