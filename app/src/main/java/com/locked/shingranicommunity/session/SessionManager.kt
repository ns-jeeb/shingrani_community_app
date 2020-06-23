package com.locked.shingranicommunity.session

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.locked.shingranicommunity.BuildConfig
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.di.AppScope
import com.locked.shingranicommunity.locked.models.User
import javax.inject.Inject

@SuppressLint("ApplySharedPref")
@AppScope
class SessionManager @Inject constructor(private val app: Application) : Session {

    private val KEY_ADMIN_LIST: String = "KEY_ADMIN_LIST"
    private val KEY_TEMPLATE_EVENT: String = "KEY_TEMPLATE_EVENT"
    private val KEY_TEMPLATE_ANNOUNCEMENT: String = "KEY_TEMPLATE_ANNOUNCEMENT"
    private val KEY_SESSION_USER: String = "KEY_SESSION_USER"
    private val KEY_SESSION_TOKEN: String = "KEY_SESSION_TOKEN"

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
    private val session: SessionData = SessionData()
    private val _logoutEvent: MutableLiveData<Boolean> = MutableLiveData()
    private val _loginState: MutableLiveData<Boolean> = MutableLiveData()
    override val loginState: LiveData<Boolean> = _loginState
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    init {
        initializeSession()
        initializeState()
    }

    private fun initializeSession() {
        val sessionUser = preferences.getString(KEY_SESSION_USER, null)
        val sessionToken: String = preferences.getString(KEY_SESSION_TOKEN, "")!!
        if (sessionUser == null) {
            session.user = null
            session.isAdmin = false
            session.isLoggedIn = false
            session.token = ""
        } else {
            session.user = Gson().fromJson(sessionUser, User::class.java)
            session.isAdmin = isAdmin(session.user!!)
            session.isLoggedIn = true
            session.token = sessionToken
        }
    }

    private fun initializeState() {
        _logoutEvent.value = !session.isLoggedIn
        _loginState.value = session.isLoggedIn
    }

    private fun postLoggedIn() {
        if (_loginState.value == false) {
            _loginState.postValue(true)
        }
        if (_logoutEvent.value == true) {
            _logoutEvent.postValue(false)
        }
    }

    private fun postLoggedOut() {
        if (_logoutEvent.value == false) {
            _logoutEvent.postValue(true)
        }
        if (_loginState.value == true) {
            _loginState.postValue(false)
        }
    }

    fun isAdmin(user: User): Boolean {
        val admins = getAdminList()
        for (admin in admins) {
            if (user._id == admin._id) return true
        }
        return false
    }

    fun setAdminList(adminList: List<User> = emptyList()) {
        preferences.edit().putString(KEY_ADMIN_LIST, Gson().toJson(adminList)).commit()
        initializeSession()
    }

    fun getAdminList() : List<User> {
        val adminList = preferences.getString(KEY_ADMIN_LIST, null)
        if (!adminList.isNullOrEmpty()) {
            val userListType = object : TypeToken<List<User>>() {}.type
            return Gson().fromJson(adminList, userListType)
        }
        return mutableListOf()
    }

    fun setLoggedInUser(user: User?) {
        user?.let {
            preferences.edit().putString(KEY_SESSION_USER, Gson().toJson(user)).commit()
            // todo remove when the whole app uses the SessionManager
            app.getSharedPreferences(Constant_Utils.SHARED_PREF_CURRENT_USER, Context.MODE_PRIVATE)
                .edit()
                .putString(Constant_Utils.CURRENT_USER, Gson().toJson(user))
                .apply()
        }
        initializeSession()
    }

    fun setSessionToken(token: String?) {
        var _token = ""
        token?.let { _token = token }
        preferences.edit().putString(KEY_SESSION_TOKEN, _token).commit()
        initializeSession()
        postLoggedIn()

        // todo remove when the whole app uses the SessionManager
        app.getSharedPreferences("token", Context.MODE_PRIVATE)
            .edit()
            .putString("token", _token).apply()
    }

    fun setTemplateIds(eventTemplate: String, announcementTemplate: String) {
        preferences.edit()
            .putString(KEY_TEMPLATE_EVENT, eventTemplate)
            .putString(KEY_TEMPLATE_ANNOUNCEMENT, announcementTemplate)
            .commit()
        initializeSession()
    }

    fun logout() {
        preferences.edit().clear().commit()
        initializeSession()
        postLoggedOut()
    }

    override fun getToken(): String {
        return session.token
    }

    override fun getUser(): User? {
        return session.user
    }

    override fun getUserId(): String? {
        return session.user?._id
    }

    override fun getAppId(): String {
        return session.appId
    }

    override fun getFullName(): String? {
        return session.user?.name
    }

    override fun getUsername(): String? {
        return session.user?.username
    }

    override fun getEventTemplateId(): String {
        return preferences.getString(KEY_TEMPLATE_EVENT, "")!!
    }

    override fun getAnnouncementTemplateId(): String {
        return preferences.getString(KEY_TEMPLATE_ANNOUNCEMENT, "")!!
    }

    override fun isUserAdmin(): Boolean {
        return session.isAdmin
    }

    override fun isLoggedIn(): Boolean {
        return session.isLoggedIn
    }
}

class SessionData {
    var isLoggedIn: Boolean = false
    var isAdmin: Boolean = false
    var user: User? = null
    var token: String = ""
    var appId: String = BuildConfig.LOCKEDAPI_APP_ID
}