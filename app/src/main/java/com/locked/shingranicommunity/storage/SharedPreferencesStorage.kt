package com.locked.shingranicommunity.storage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.locked.shingranicommunity.Constant_Utils.ADMIN_USER
import com.locked.shingranicommunity.Constant_Utils.CURRENT_USER
import com.locked.shingranicommunity.Constant_Utils.LIST_USER
import com.locked.shingranicommunity.Constant_Utils.SHARED_PREF_ADMIN_USER
import com.locked.shingranicommunity.Constant_Utils.SHARED_PREF_CURRENT_USER
import com.locked.shingranicommunity.Constant_Utils.SHARED_PREF_LIST_USER
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.models.TemplateModel
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
        var sharePr = context.getSharedPreferences(SHARED_PREF_CURRENT_USER,Context.MODE_PRIVATE).edit()
        sharePr.putString(CURRENT_USER, Gson().toJson(user)).apply()
    }

    override fun getCurrentUser(): User? {
        var sharePr = context.getSharedPreferences(SHARED_PREF_CURRENT_USER,Context.MODE_PRIVATE)
        return Gson().fromJson<User>(sharePr.getString(CURRENT_USER,""))
    }

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)

//    override fun getUser(): MutableLiveData<ArrayList<ShingraniMember>> {
//        var users = MutableLiveData<ArrayList<ShingraniMember>>()
//        var sharePr = context.getSharedPreferences(SHARED_PREF_LIST_USER,Context.MODE_PRIVATE)
//        var type = Gson().fromJson<ArrayList<ShingraniMember>>(sharePr.getString(LIST_USER,""))
//        users.value = type
//        return users
//    }
//    override fun setUser(users: ArrayList<ShingraniMember>) {
//        var sharePr = context.getSharedPreferences(SHARED_PREF_LIST_USER,Context.MODE_PRIVATE).edit()
//        sharePr.putString(LIST_USER,Gson().toJson(users)).apply()
//
//    }

    override fun getAdminUser(): MutableLiveData<TemplateModel> {
        var admin = MutableLiveData<TemplateModel>()
        var sharedPr = context.getSharedPreferences(SHARED_PREF_ADMIN_USER,Context.MODE_PRIVATE)
        var type = Gson().fromJson<TemplateModel>(sharedPr.getString(ADMIN_USER,""))
        admin.value = type
        return admin
    }

    override fun setAdminUser(adminUser: TemplateModel) {
        var sharedPr = context.getSharedPreferences(SHARED_PREF_ADMIN_USER,Context.MODE_PRIVATE).edit()
        sharedPr.putString(ADMIN_USER,Gson().toJson(adminUser)).apply()
    }
}