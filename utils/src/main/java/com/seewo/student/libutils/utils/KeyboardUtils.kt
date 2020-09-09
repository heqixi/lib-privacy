package com.seewo.student.libutils.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class KeyboardUtils {

    companion object {

        private const val TAG = "KeyboardUtils"

        fun showKeyboard(context: Context, editText: EditText?) {
            try {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, 0)
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }

        fun hideSoftKeyBoard(activity: Activity) {
            try {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val view: View? = activity.window.currentFocus
                if (null != view) {
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
    }
}