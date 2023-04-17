package com.locked.shingranicommunity.common

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int) : String
    fun getString(@StringRes resId: Int, vararg args: String) : String
    fun getStringArray(@ArrayRes resId: Int) : Array<String>
}