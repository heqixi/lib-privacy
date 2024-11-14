package com.seewo.eclass.privacy.common.ext

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.seewo.eclass.privacy.PrivacyActivity
import com.seewo.eclass.privacy.PrivacyCore
import com.seewo.eclass.privacy.PrivacyDetailActivity
import com.seewo.eclass.privacy.R
import com.seewo.eclass.privacy.common.helper.PrivacyPreferenceUtil

/**
 * http://uxc.gz.cvte.cn/#/view/c3886f9aca711e786edbfed1a49b86d0
 *
 * privacyVersionName:          隐私协议版本号，必须定义，当协议内容发生变更时，版本号需要修改。建议版本号：V0.0.1、V0.0.2 ...
 * privacyPrimaryColorId:       主题色。例如 按钮的颜色，学习机的主题色为：8F8FFF
 * privacyDesc:                 协议同意Dialog弹窗的描述，默认为：视睿重视对未成年人...
 * privacyDetailTitle:          协议详情Dialog弹窗的标题，默认为：希沃学习机许可及服务协议
 * privacyDetailAssetPath:      协议同意Dialog弹窗的详情。默认asset文件为：class-privacy/privacy.html
 *
 */
fun Application.invokePrivacyDisplay(
    privacyVersionName: String,
    privacyPrimaryColorId: Int = R.color.privacy_primary,
    privacyDesc: String = getString(R.string.privacy_desc),
    privacyDetailTitle: String = getString(R.string.privacy_detail_title),
    privacyDetailAssetPath: String = "class-privacy/privacy.html",
    ignoreActivityList: List<String> = emptyList(),
    onpPrivacyAgreeCallback: () -> Unit
) {
    PrivacyCore.applicationContext = this
    PrivacyCore.privacyVersionName = privacyVersionName
    PrivacyCore.privacyPrimaryColor = this.getColor(privacyPrimaryColorId)
    PrivacyCore.privacyDesc = privacyDesc
    PrivacyCore.privacyAssetPath = privacyDetailAssetPath
    PrivacyCore.privacyDetailTitle = privacyDetailTitle
    if (PrivacyPreferenceUtil.instance.isPrivacyAgreed()) {
        onpPrivacyAgreeCallback.invoke()
    } else {
        attachActivityCreated(ignoreActivityList, onpPrivacyAgreeCallback)
    }
}

private fun Application.attachActivityCreated(ignoreActivityList: List<String>, onPrivacyAgreeCallback: () -> Unit) {
    PrivacyActivity.onAgreeLivaData.observeForever {
        onPrivacyAgreeCallback.invoke()
    }
    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, p1: Bundle?) {
            if (PrivacyPreferenceUtil.instance.isPrivacyAgreed()) return
            if (activity is PrivacyActivity || activity is PrivacyDetailActivity) return
            if (activity::class.java.name in ignoreActivityList) return
            PrivacyActivity.startSelf(activity)
            activity.finish()
        }

        override fun onActivityStarted(p0: Activity) {}
        override fun onActivityResumed(p0: Activity) {}
        override fun onActivityPaused(p0: Activity) {}
        override fun onActivityStopped(p0: Activity) {}
        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
        override fun onActivityDestroyed(p0: Activity) {}
    })
}