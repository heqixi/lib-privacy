package com.seewo.student.libutils.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

/**
 * Created by linkaipeng on 2020/8/27.
 *
 */
object AppUtils {

    fun launcherActivityWithPkg(context: Context?, pkgName: String?) {
        if (context == null || pkgName.isNullOrEmpty()) return
        val intent = context.packageManager?.getLaunchIntentForPackage(pkgName)
        intent?.let { launcherLocalAppWithIntent(context, it) }
    }

    fun launcherActivityWithAction(
        context: Context?,
        action: String?,
        packageName: String? = null,
        extras: Bundle? = null
    ) {
        if (context == null || action.isNullOrEmpty()) return
        val intent = Intent(action)
        packageName?.let {
            intent.`package` = packageName
        }
        launcherLocalAppWithIntent(context, Intent(action).apply {
            extras?.let { putExtras(it) }
        })
    }

    fun launcherLocalAppWithIntent(context: Context, intent: Intent): Boolean {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        try {
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getAppNameFromPackageName(context: Context, packageName: String): String {
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return applicationInfo.loadLabel(packageManager).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    fun getAppVersionName(context: Context, packageName: String): String {
        try {
            return context.packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}
