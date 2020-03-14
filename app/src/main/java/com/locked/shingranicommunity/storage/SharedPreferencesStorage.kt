

package com.locked.shingranicommunity.storage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.members.User
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

    override fun getUser(): MutableLiveData<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUser(users: ArrayList<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
