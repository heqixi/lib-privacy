package com.seewo.eclass.privacy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.seewo.eclass.privacy.common.helper.PrivacyPreferenceUtil
import com.seewo.eclass.privacy.common.helper.ScreenUtil
import com.seewo.eclass.privacy.dialog.PrivacyAgreeDialog
import kotlin.system.exitProcess

class PrivacyActivity : AppCompatActivity() {

    companion object {
        private const val KEY_CLASS = "extra_class"
        private const val KEY_INTENT = "extra_intent"
        val onAgreeLivaData = MutableLiveData<Boolean>()

        fun startSelf(activity: Activity) {
            activity.startActivity(Intent(activity, PrivacyActivity::class.java).apply {
                putExtra(KEY_CLASS, activity.javaClass.name)
                putExtra(KEY_INTENT, activity.intent)
            })
        }
    }

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    private var extraClass: String = ""
    private var extraIntent: Intent? = null

    private val privacyAgreeDialog by lazy {
        PrivacyAgreeDialog(
            attachContext = this,
            primaryColor = PrivacyCore.privacyPrimaryColor,
            appNameRes = PrivacyCore.privacyAppNameRes,
            appIconRes = PrivacyCore.privacyAppIconRes,
            privacyDesc = PrivacyCore.privacyDesc,
            privacyDetailTitle = PrivacyCore.privacyDetailTitle,
            privacyAssetPath = PrivacyCore.privacyAssetPath,
            onDisagreeCallback = {
                PrivacyPreferenceUtil.instance.setPrivacyAgree(false)
                finish()
                postKillProcessWhenDisAgree()
            },
            onAgreeCallback = {
                PrivacyPreferenceUtil.instance.setPrivacyAgree(true)
                onAgreeLivaData.value = true
                startActivity(Intent(this, Class.forName(extraClass)).apply {
                    extraIntent?.let {
                        putExtras(it)
                        data = it.data
                    }
                })
                finish()
            }
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtil.hideNavigationBar(this)
        setContentView(ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundColor(resources.getColor(R.color.privacy_gray_f8, null))
        })
        extraClass = this.intent.getStringExtra(KEY_CLASS) ?: run {
            this.finish()
            return
        }
        extraIntent = this.intent.getParcelableExtra(KEY_INTENT)
        mainHandler.post {
            showPrivacyAgreeDialog()
        }
    }

    private fun showPrivacyAgreeDialog() {
        privacyAgreeDialog.show()
        privacyAgreeDialog.window?.apply {
            addFlags(FLAG_TRANSLUCENT_STATUS)
            statusBarColor = Color.TRANSPARENT
        }
    }

    private fun postKillProcessWhenDisAgree() {
        mainHandler.postDelayed({
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }, 300)
    }
}