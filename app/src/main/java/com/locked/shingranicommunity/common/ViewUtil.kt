package com.locked.shingranicommunity.common

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object ViewUtil {

    fun hideKeyboard(activity: Activity?) {
        val imm: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(activity?.window?.decorView?.rootView?.windowToken, 0)
    }
}