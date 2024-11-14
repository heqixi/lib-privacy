package com.seewo.eclass.privacy.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.seewo.eclass.privacy.PrivacyCore
import com.seewo.eclass.privacy.R
import com.seewo.eclass.privacy.common.ext.initPrivacyAlphaClick
import com.seewo.eclass.privacy.common.ext.privacyRadius
import com.seewo.eclass.privacy.common.ext.privacy_dp
import com.seewo.eclass.privacy.common.helper.SystemHelper
import com.seewo.eclass.privacy.databinding.PrivacyDialogPrivacyDetailBinding
import com.seewo.eclass.privacy.databinding.PrivacyDialogPrivacyMainBinding
import com.seewo.eclass.privacy.databinding.PrivacyDialogRootBinding

class PrivacyAgreeDialog(
    private val attachContext: Context,
    private val primaryColor: Int,
    private val appNameRes: Int,
    private val appIconRes: Int,
    private val privacyDesc: String,
    private val permissionList: List<String>? = null,
    private val permissionDescList: List<String>? = null,
    private val privacyDetailTitle: String = PrivacyCore.privacyDetailTitle,
    private val privacyAssetPath: String = PrivacyCore.privacyAssetPath,
    private val onDisagreeCallback: () -> Unit = {},
    private val onAgreeCallback: () -> Unit = {}
) : Dialog(attachContext, R.style.PrivacyDialogStyle) {
    private val privacyRootView by lazy {
        PrivacyDialogRootBinding.inflate(layoutInflater)
    }
    private val agreeBinding by lazy {
        PrivacyDialogPrivacyMainBinding.inflate(layoutInflater)
    }
    private val detailBinding by lazy {
        PrivacyDialogPrivacyDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(privacyRootView.root)
        privacyRootView.rootView.apply {
            addView(
                agreeBinding.root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                detailBinding.root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            detailBinding.root.isVisible = false
        }
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        agreeBinding.privacyMessageView.movementMethod = ScrollingMovementMethod.getInstance()
        agreeBinding.privacyPointDetailView.setTextColor(primaryColor)
        agreeBinding.privacyAgreeButton.setBackgroundColor(primaryColor)
        agreeBinding.privacyAgreeCheckBox.buttonDrawable = getCheckBoxStateListDrawable()
        agreeBinding.privacyAgreeButton.privacyRadius(24F.privacy_dp)
        val appName = if (appNameRes == -1) {
            SystemHelper.getAppName(context, context.packageName)
        } else {
            attachContext.getString(appNameRes)
        }
        agreeBinding.privacyTitleView.text = context.getString(R.string.privacy_welcome_app, appName)
        agreeBinding.privacyMessageView.text = privacyDesc
        agreeBinding.privacyPointDetailView.text = context.getString(R.string.privacy_detail_point, appName)
        if (appIconRes == -1) {
            agreeBinding.privacyIconView.setImageDrawable(SystemHelper.getAppIcon(context, context.packageName))
        } else {
            agreeBinding.privacyIconView.setImageResource(appIconRes)
        }
        agreeBinding.privacyPermissionLayout.initData(permissionList, permissionDescList, primaryColor)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        agreeBinding.privacyPointDetailView.initPrivacyAlphaClick {
            agreeBinding.root.isVisible = false
            detailBinding.root.isVisible = true
            loadPrivacyWebView()
        }
        agreeBinding.privacyPermissionLayout.onAllCheckChangeCallback = {
            agreeBinding.privacyAgreeButton.isEnabled = agreeBinding.privacyAgreeCheckBox.isChecked && agreeBinding.privacyPermissionLayout.isAllChecked()
        }
        agreeBinding.privacyAgreeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            agreeBinding.privacyAgreeButton.isEnabled = isChecked && agreeBinding.privacyPermissionLayout.isAllChecked()
        }
        agreeBinding.privacyDisagreeButton.initPrivacyAlphaClick {
            this.dismiss()
            this.onDisagreeCallback.invoke()
        }
        agreeBinding.privacyAgreeButton.initPrivacyAlphaClick(supportEnableStyle = true) {
            this.dismiss()
            this.onAgreeCallback.invoke()
        }
        agreeBinding.privacyAgreeButton.isEnabled = agreeBinding.privacyAgreeCheckBox.isChecked

        initPrivacyDetailView()
    }

    private fun getCheckBoxStateListDrawable(): StateListDrawable {
        val normalDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.privacy_checkbox_normal, null)
        val checkedDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.privacy_checkbox_checked, null)?.apply {
            setTint(primaryColor)
        }
        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_checked), checkedDrawable)
            addState(intArrayOf(), normalDrawable)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initPrivacyDetailView() {
        detailBinding.detailTitleView.text = privacyDetailTitle
        detailBinding.detailContainFrameLayout.privacyRadius(24F.privacy_dp)
        detailBinding.detailConfirmButton.setBackgroundColor(primaryColor)
        detailBinding.detailConfirmButton.privacyRadius(24F.privacy_dp)
        detailBinding.detailConfirmButton.initPrivacyAlphaClick {
            agreeBinding.root.isVisible = true
            detailBinding.root.isVisible = false
        }
        detailBinding.privacyWebView.setBackgroundColor(ContextCompat.getColor(context, R.color.privacy_gray_f8))
        val webSettings: WebSettings = detailBinding.privacyWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.allowFileAccess = true
        webSettings.loadWithOverviewMode = true
        webSettings.pluginState = WebSettings.PluginState.ON
        detailBinding.privacyWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        detailBinding.privacyWebView.setOnLongClickListener {
            return@setOnLongClickListener true
        }
        detailBinding.detailRevokeButton.visibility = View.GONE
    }

    private fun loadPrivacyWebView() {
        detailBinding.privacyWebView.post {
            detailBinding.privacyWebView.loadUrl("file:///android_asset/${privacyAssetPath}")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && detailBinding.root.isVisible && detailBinding.privacyWebView.canGoBack()) {
            detailBinding.privacyWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}