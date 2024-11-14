package com.seewo.eclass.privacy.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.webkit.WebView
import androidx.annotation.Keep

/**
 * V1 上面，首次使用显示第一个隐私协议弹窗，有 webview 会导致弹窗 AutoSize 失效，使用此方式可以解决
 * https://github.com/JessYanCoding/AndroidAutoSize/issues/5
 */
@Keep
class AutoSizeWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : WebView(context, attrs) {

    override fun setOverScrollMode(mode: Int) {
        super.setOverScrollMode(mode)
        invokeAutoSizeCompatAutoConvertDensityOfGlobal()
    }

    private fun invokeAutoSizeCompatAutoConvertDensityOfGlobal() {
        try {
            val clazz = Class.forName("me.jessyan.autosize.AutoSizeCompat")
            val autoConvertDensityOfGlobalMethod = clazz.getMethod("autoConvertDensityOfGlobal", Resources::class.java)
            autoConvertDensityOfGlobalMethod.invoke(null, super.getResources())
        } catch (e: Exception) {
            // NOOP
        }
    }
}