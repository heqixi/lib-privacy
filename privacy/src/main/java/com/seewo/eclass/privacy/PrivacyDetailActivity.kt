package com.seewo.eclass.privacy

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.view.View
import android.view.WindowManager
import com.seewo.eclass.privacy.common.helper.ScreenUtil
import com.seewo.eclass.privacy.common.helper.SystemHelper.hasFileInAsserts
import com.seewo.eclass.privacy.dialog.PrivacyDetailDialog
import kotlin.system.exitProcess

class PrivacyDetailActivity : Activity() {

    companion object {
        private const val KEY_SETTINGS_INTENT = "extra_settings_intent"
        private const val KEY_TITLE_EXTRA = "extra_title"
        private const val KEY_REVOKE_PRIVACY_INTENT = "extra_revoke_privacy_intent"
        private const val SHOW_PRIVACY_DETAIL_INTENT = "SHOW_PRIVACY_DETAIL_INTENT"
        private const val SHOW_SHARE_LIST_DETAIL_INTENT = "SHOW_SHARE_LIST_DETAIL_INTENT"
        private const val SHOW_SDK_LIST_DETAIL_INTENT = "SHOW_SDK_LIST_DETAIL_INTENT"
        private const val BROADCAST_REVOKE_PRIVACY = "com.seewo.eclass.application.ACTION_PRIVACY_REVOKE"

        private const val FILE_ROOT = "class-privacy"
        private const val SHARE_LIST_FILE_NAME = "ss_share_list.html"
        private const val SDK_LIST_FILE_NAME   = "ss_sdk_list.html"
    }

    private var settingsExtra: String? = null
    private var titleExtra: String? = null

    private val mPrivacyDetailDialog: PrivacyDetailDialog by lazy {
        PrivacyDetailDialog(
            attachContext = this,
            privacyDetailTitle = PrivacyCore.privacyDetailTitle,
            privacyAssetPath = PrivacyCore.privacyAssetPath,
            primaryColor = PrivacyCore.privacyPrimaryColor,
            onPrivacyConfirmCallback = {
                mPrivacyDetailDialog.dismiss()
                finish()
            },
            onPrivacyRevokeCallback = {
                sendBroadcast(Intent().apply {
                    action = BROADCAST_REVOKE_PRIVACY
                    putExtra(KEY_REVOKE_PRIVACY_INTENT, this@PrivacyDetailActivity.packageName)
                })
                mPrivacyDetailDialog.dismiss()
                finish()
            }
        )
    }

    private val mShareListDetailDialog: PrivacyDetailDialog by lazy {
        PrivacyDetailDialog(
            attachContext = this,
            privacyDetailTitle = titleExtra ?: getString(R.string.privacy_share_list_title),
            privacyAssetPath = "$FILE_ROOT/$SHARE_LIST_FILE_NAME",
            primaryColor = PrivacyCore.privacyPrimaryColor,
            onPrivacyConfirmCallback = {
                mShareListDetailDialog.dismiss()
                finish()
            }
        )
    }

    private val mSDKListDetailDialog: PrivacyDetailDialog by lazy {
        PrivacyDetailDialog(
            attachContext = this,
            privacyDetailTitle = titleExtra ?: getString(R.string.privacy_sdk_list_title),
            privacyAssetPath = "$FILE_ROOT/$SDK_LIST_FILE_NAME",
            primaryColor = PrivacyCore.privacyPrimaryColor,
            onPrivacyConfirmCallback = {
                mSDKListDetailDialog.dismiss()
                finish()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtil.hideNavigationBar(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setContentView(View(this))

        //settings 应用详情传入的intent
        settingsExtra = intent.getStringExtra(KEY_SETTINGS_INTENT)
        titleExtra = intent.getStringExtra(KEY_TITLE_EXTRA)
        if (settingsExtra == null) {
            postKillProcess()
        }
        when(settingsExtra) {
            SHOW_PRIVACY_DETAIL_INTENT -> {
                mPrivacyDetailDialog.show()
                mPrivacyDetailDialog.window?.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    statusBarColor = Color.TRANSPARENT
                }
            }
            SHOW_SHARE_LIST_DETAIL_INTENT -> {
                if (!hasFileInAsserts(SHARE_LIST_FILE_NAME, FILE_ROOT)) {
                    postKillProcess()
                }
                mShareListDetailDialog.show()
                mShareListDetailDialog.window?.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    statusBarColor = Color.TRANSPARENT
                }
            }
            SHOW_SDK_LIST_DETAIL_INTENT -> {
                if (!hasFileInAsserts(SDK_LIST_FILE_NAME, FILE_ROOT)) {
                    postKillProcess()
                }
                mSDKListDetailDialog.show()
                mSDKListDetailDialog.window?.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    statusBarColor = Color.TRANSPARENT
                }
            }
            else -> postKillProcess()
        }
    }

    private fun postKillProcess() {
        finish()
        Handler(Looper.getMainLooper()).postDelayed({
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }, 300)
    }

    override fun onPause() {
        super.onPause()
        when(settingsExtra) {
            SHOW_PRIVACY_DETAIL_INTENT -> mPrivacyDetailDialog.dismiss()
            SHOW_SHARE_LIST_DETAIL_INTENT -> mShareListDetailDialog.dismiss()
            SHOW_SDK_LIST_DETAIL_INTENT -> mSDKListDetailDialog.dismiss()
        }
        finish()
    }

}