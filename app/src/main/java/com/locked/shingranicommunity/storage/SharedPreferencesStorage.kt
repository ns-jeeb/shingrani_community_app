

package com.locked.shingranicommunity.storage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SharedPreferencesStorage @Inject constructor(val context: Context) : Storage {

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

    override fun setCurrentUser(user: User) {
        var sharePr = context.getSharedPreferences("user",Context.MODE_PRIVATE).edit()
        var json: Gson = Gson()
        sharePr.putString("member",json.toJson(user)).apply()
    }

    override fun getCurrentUser(): User? {
        var sharePr = context.getSharedPreferences("user",Context.MODE_PRIVATE)
        var json: Gson = Gson()
        var type = json.fromJson<User>(sharePr.getString("member",""))
        return type
    }

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)
    override fun getUser(): MutableLiveData<ArrayList<ShingraniMember>> {

        var users = MutableLiveData<ArrayList<ShingraniMember>>()
        var sharePr = context.getSharedPreferences("user_list",Context.MODE_PRIVATE)
        var json: Gson = Gson()
        var type = Gson().fromJson<ArrayList<ShingraniMember>>(sharePr.getString("users",""))
        users.value = type
        return users
    }
    override fun setUser(users: ArrayList<ShingraniMember>) {
        var sharePr = context.getSharedPreferences("user_list",Context.MODE_PRIVATE).edit()
        var json: Gson = Gson()
        sharePr.putString("users",json.toJson(users)).apply()

    }
}

