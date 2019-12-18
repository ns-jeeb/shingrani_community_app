

package com.locked.shingranicommunity.storage

import android.content.Context
import com.locked.shingranicommunity.di.Storage
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(context: Context) : Storage {

    private val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)

    override fun setToken(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getToken(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }
}
