package com.locked.shingranicommunity.common

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int) : String
}