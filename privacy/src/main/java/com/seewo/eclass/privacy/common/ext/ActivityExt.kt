package com.seewo.eclass.privacy.common.ext

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import com.seewo.eclass.privacy.PrivacyCore
import com.seewo.eclass.privacy.R
import com.seewo.eclass.privacy.dialog.PrivacyAgreeDialog

/**
 * 当需要自定义应用名称和应用图标时，需要特殊传入ResId，否则会默认通过包名读取。
 */
fun Activity.showPrivacyAgreeDialog(
    isOverlayApplication: Boolean = false,
    privacyAppNameRes: Int = -1,
    privacyAppIconRes: Int = -1,
    privacyPrimaryColorId: Int = R.color.privacy_primary,
    privacyDesc: String = getString(R.string.privacy_desc),
    privacyDetailTitle: String = getString(R.string.privacy_detail_title),
    privacyPermissionList: List<String>? = null,
    privacyPermissionDescList: List<String>? = null,
    privacyDetailAssetPath: String = "class-privacy/privacy.html",
    onpPrivacyDisagreeCallback: () -> Unit,
    onpPrivacyAgreeCallback: (privacyPermissionList: List<String>?) -> Unit
) {
    PrivacyCore.applicationContext = this.applicationContext
    PrivacyAgreeDialog(
        attachContext = this,
        primaryColor = this.getColor(privacyPrimaryColorId),
        appNameRes = privacyAppNameRes,
        appIconRes = privacyAppIconRes,
        privacyDesc = privacyDesc,
        permissionList = privacyPermissionList,
        permissionDescList = privacyPermissionDescList,
        privacyDetailTitle = privacyDetailTitle,
        privacyAssetPath = privacyDetailAssetPath,
        onDisagreeCallback = {
            onpPrivacyDisagreeCallback.invoke()
        },
        onAgreeCallback = {
            onpPrivacyAgreeCallback.invoke(privacyPermissionList)
        }
    ).apply {
        if (isOverlayApplication && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }
    }.show()
}