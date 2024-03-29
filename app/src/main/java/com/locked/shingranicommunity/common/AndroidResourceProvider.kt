package com.locked.shingranicommunity.common

import android.app.Application
import javax.inject.Inject

class AndroidResourceProvider @Inject constructor(val application: Application) : ResourceProvider {

    override fun getString(resId: Int): String {
        return application.getString(resId)
    }

    override fun getString(resId: Int, vararg args: String): String {
        return application.getString(resId, args)
    }

    override fun getStringArray(resId: Int): Array<String> {
        return application.resources.getStringArray(resId)
    }
}