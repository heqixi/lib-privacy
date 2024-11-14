package com.seewo.eclass.utils

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import com.seewo.eclass.privacy.common.ext.showPrivacyAgreeDialog
import com.seewo.eclass.utils.databinding.LoginActivityPrivacyAgreeBinding
import java.util.concurrent.LinkedBlockingQueue

class PrivacyAgreeActivity : AppCompatActivity() {

    companion object {
        const val TAG = "PrivacyAgreeActivity"
        val URL_POLICY = Pair(
            "",
            "policy.html"
        )
        val URL_AGREEMENT = Pair(
            "",
            "agreement.html"
        )
        val URL_CHILD_PRIVACY = Pair(
            "",
            "child_privacy.html"
        )
        val URL_SDK = Pair(
            "",
            "sdk.html"
        )
    }

    private val overrideUrlMap by lazy {
        mapOf(
            "https://account.seewo.com/seewo-account/agreement?appCode=study-station-android&type=0&index=0" to "file:///android_asset/policy.html",
            "https://account.seewo.com/seewo-account/agreement?appCode=study-station-android&type=0&index=1" to "file:///android_asset/child_privacy.html",
            "https://account.seewo.com/seewo-account/agreement?appCode=study-station-android&type=0&index=3" to "file:///android_asset/sdk.html"
        )
    }

    private val binding by lazy { LoginActivityPrivacyAgreeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val uncheckAgreements = LinkedBlockingQueue<Pair<String, String>>()
        uncheckAgreements.add(URL_AGREEMENT)
        uncheckAgreements.add(URL_POLICY)
        uncheckAgreements.add(URL_CHILD_PRIVACY)
        showAgreementsDialog(uncheckAgreements) {
            overridePendingTransition(0, 0)
        }
    }

    private fun showAgreementsDialog(agreementGroup: LinkedBlockingQueue<Pair<String, String>>, onAllAgreed: (() -> Unit)) {
        val agreementUrl = agreementGroup.poll()
        if (agreementUrl == null) {
            onAllAgreed.invoke()
        } else {
            showPrivacyAgreeDialog(
                privacyAppNameRes = R.string.app_name,
                privacyAppIconRes = R.drawable.login_brand_icon,
                privacyPrimaryColorId = R.color.colorPrimary,
                privacyDesc = getString(R.string.login_privacy_desc),
                privacyDetailTitle = getString(R.string.app_name),
                privacyDetailAssetPath = "agreement.html",
                onpPrivacyDisagreeCallback = {
                    showCheckAgreementFailedDialog() {
                        val newAgreementGroup = LinkedBlockingQueue<Pair<String, String>>().apply {
                            add(agreementUrl)
                            addAll(agreementGroup)
                        }
                        showAgreementsDialog(newAgreementGroup, onAllAgreed)
                    }
                },
                onpPrivacyAgreeCallback = {
                    onAllAgreed.invoke()
                },
            )

//            LoginDialogHelper.showSingleAgreementDialog(
//                activity = this,
//                agreementUrls = agreementUrl,
//                onUnAgreeClicked = {
//                    showCheckAgreementFailedDialog() {
//                        val newAgreementGroup = LinkedBlockingQueue<Pair<String, String>>().apply {
//                            add(agreementUrl)
//                            addAll(agreementGroup)
//                        }
//                        showAgreementsDialog(newAgreementGroup, onAllAgreed)
//                    }
//                },
//                onAgreeClicked = {
//                    showAgreementsDialog(agreementGroup, onAllAgreed)
//                })
        }
    }

    private fun showCheckAgreementFailedDialog(onCancelCallback: () -> Unit) {
//        MainPointDialog.Builder(
//            context = this,
//            pointTitle = getString(R.string.login_privacy_un_agree_title),
//            pointMessage = getString(R.string.login_privacy_un_agree_message),
//            negativeMessage = getString(R.string.common_cancel),
//            neutralMessage = getString(R.string.login_privacy_un_agree_power_off),
//            pointNegativeAction = {
//                onCancelCallback.invoke()
//            },
//            pointNeutralAction = {
//                shutDownDevice()
//            }
//        ).build().apply {
//          setCancelable(false)
//        }.show()
    }

//    private fun shutDownDevice() {
//        try {
//            val powerManager: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
//            com.tsingbei.student.libutils.launcher.reflect.Reflect.on(powerManager).call("shutdown", false, "userrequested", false)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    override fun onResume() {
        super.onResume()
//        SystemHelper.disableHomeRecentGesture(true)
    }

    override fun onPause() {
        super.onPause()
//        SystemHelper.disableHomeRecentGesture(false)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}