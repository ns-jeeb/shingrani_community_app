package com.locked.shingranicommunity.common

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int) : String
    fun getStringArray(@ArrayRes resId: Int) : Array<String>
}