package com.seewo.eclass.privacy.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.seewo.eclass.privacy.R
import com.seewo.eclass.privacy.databinding.PrivacyViewSwitchTextBinding


class SwitchTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding by lazy {
        PrivacyViewSwitchTextBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var onCheckChangeCallback: (Boolean) -> Unit = {}

    init {
        binding.privacySwitcher.setOnCheckedChangeListener { _, isChecked ->
            onCheckChangeCallback.invoke(isChecked)
        }
    }

    fun initData(desc: String, color: Int) {
        binding.privacySwitchText.text = desc
        binding.privacySwitcher.trackDrawable = getStateListDrawable(color)
    }

    fun isChecked(): Boolean {
        return binding.privacySwitcher.isChecked
    }

    private fun getStateListDrawable(primaryColor: Int): StateListDrawable {
        val normalDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.privacy_switch_track_normal, null)
        val checkedDrawable = (ResourcesCompat.getDrawable(context.resources, R.drawable.privacy_switch_track_check, null) as? GradientDrawable)?.apply {
            setColor(primaryColor)
        }
        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_checked), checkedDrawable)
            addState(intArrayOf(), normalDrawable)
        }
    }




}