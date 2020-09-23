package com.seewo.student.libutils.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object KeyboardUtils {

    fun View.showKeyboard() {
        context.getInputMethodManager()?.showSoftInput(this, 0)
    }

    fun View.hideKeyboard() {
        context.getInputMethodManager()?.hideSoftInputFromWindow(windowToken, 0)
    }

    fun Activity.hideKeyboard() {
        window.currentFocus?.let {
            getInputMethodManager()?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun Fragment.hideKeyboard() {
        activity?.hideKeyboard()
    }

    private fun Context?.getInputMethodManager(): InputMethodManager? {
        return this?.applicationContext?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
    }
}