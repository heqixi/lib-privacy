package com.seewo.eclass.privacy.common.helper

import android.content.Context
import com.seewo.eclass.privacy.PrivacyCore

class PrivacyPreferenceUtil {

    companion object {
        private const val PREFERENCE_NAME = "ss_ui_share_config"
        private const val KEY_SKIN_NAME = "privacy_agree"

        val instance: PrivacyPreferenceUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PrivacyPreferenceUtil()
        }
    }

    val preference
        get() = PrivacyCore.applicationContext?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun isPrivacyAgreed(): Boolean {
        return preference?.getBoolean(getPrivacyKey(), false) ?: false
    }

    fun setPrivacyAgree(isAgree: Boolean) {
        preference?.edit()?.putBoolean(getPrivacyKey(), isAgree)?.apply()
    }

    private fun getPrivacyKey(): String {
        return "${KEY_SKIN_NAME}_${PrivacyCore.privacyVersionName}"
    }
}