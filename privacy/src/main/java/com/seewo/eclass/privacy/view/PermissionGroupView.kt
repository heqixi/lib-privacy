package com.seewo.eclass.privacy.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.seewo.eclass.privacy.databinding.PrivacyViewPermissionGroupBinding

/**
 * @author huangyongsen
 * @date 2022/2/25
 *
 */
class PermissionGroupView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)  {

    private val binding by lazy {
        PrivacyViewPermissionGroupBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var onAllCheckChangeCallback: (Boolean) -> Unit = {}

    fun initData(permissionList: List<String>? = null, permissionDescList: List<String>? = null, primaryColor: Int) {
        if (permissionList.isNullOrEmpty() ||
            permissionDescList.isNullOrEmpty() ||
            permissionList.size != permissionDescList.size
        ) return

        permissionDescList.forEach {
            binding.privacyPermissionLayout.addView(
                SwitchTextView(context).apply {
                    initData(it, primaryColor)
                    onCheckChangeCallback = {
                        onAllCheckChangeCallback.invoke(checkAllPermissionGranted())
                    }
                }
            )
        }
    }

    fun isAllChecked() = checkAllPermissionGranted()

    private fun checkAllPermissionGranted(): Boolean {
        val count = binding.privacyPermissionLayout.childCount
        for (i in 0 until count) {
            if (!(binding.privacyPermissionLayout.getChildAt(i) as SwitchTextView).isChecked()) {
                return false
            }
        }
        return true
    }
}