package com.seewo.eclass.privacy.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.seewo.eclass.privacy.R
import com.seewo.eclass.privacy.common.ext.initPrivacyAlphaClick
import com.seewo.eclass.privacy.common.ext.privacyRadius
import com.seewo.eclass.privacy.common.ext.privacy_dp
import com.seewo.eclass.privacy.common.helper.PrivacyPreferenceUtil
import com.seewo.eclass.privacy.databinding.PrivacyDialogPrivacyDetailBinding

class PrivacyDetailDialog(
    attachContext: Context,
    private val privacyDetailTitle: String,
    private val privacyAssetPath: String,
    private val primaryColor: Int,
    private val onPrivacyConfirmCallback: () -> Unit,
    private val onPrivacyRevokeCallback: (() -> Unit)? = null
) : Dialog(attachContext, R.style.PrivacyDialogStyle) {
    private val binding by lazy {
        PrivacyDialogPrivacyDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        binding.detailTitleView.text = privacyDetailTitle
        binding.detailContainFrameLayout.privacyRadius(24F.privacy_dp)
        binding.detailConfirmButton.setBackgroundColor(primaryColor)
        binding.detailConfirmButton.privacyRadius(32F.privacy_dp)
        binding.detailConfirmButton.initPrivacyAlphaClick {
            this.dismiss()
            this.onPrivacyConfirmCallback.invoke()
        }
        loadPrivacyDetail()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPrivacyDetail() {
        binding.privacyWebView.setBackgroundColor(ContextCompat.getColor(context, R.color.privacy_gray_f8))
        val webSettings: WebSettings = binding.privacyWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.allowFileAccess = true
        webSettings.loadWithOverviewMode = true
        webSettings.pluginState = WebSettings.PluginState.ON
        binding.privacyWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        binding.privacyWebView.setOnLongClickListener {
            return@setOnLongClickListener true
        }
        binding.privacyWebView.post {
            binding.privacyWebView.loadUrl("file:///android_asset/${privacyAssetPath}")
        }
        if (onPrivacyRevokeCallback != null && PrivacyPreferenceUtil.instance.isPrivacyAgreed()) {
            binding.detailRevokeButton.visibility = View.VISIBLE
            binding.detailRevokeButton.initPrivacyAlphaClick {
                dismiss()
                onPrivacyRevokeCallback.invoke()
            }
        } else {
            binding.detailRevokeButton.visibility = View.GONE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.privacyWebView.canGoBack()) {
            binding.privacyWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}