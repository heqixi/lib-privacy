package com.seewo.student.libutils.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

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
    return this?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
}