package com.locked.shingranicommunity.session

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.locked.shingranicommunity.BuildConfig
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
    private val _appState: MutableLiveData<Boolean> = MutableLiveData()
    override val appState: LiveData<Boolean> = _appState
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    init {
        initializeSession()
        initializeState()
    }

    private fun initializeSession() {
        session.eventTemplateId = preferences.getString(KEY_TEMPLATE_EVENT, "")!!
        session.announcementTemplateId = preferences.getString(KEY_TEMPLATE_ANNOUNCEMENT, "")!!
        val adminList = preferences.getString(KEY_ADMIN_LIST, null)
        if (!adminList.isNullOrEmpty()) {
            val userListType = object : TypeToken<List<User>>() {}.type
            session.adminList = Gson().fromJson(adminList, userListType)
        } else {
            session.adminList = null
        }
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
        _appState.value = !session.adminList.isNullOrEmpty() && !session.announcementTemplateId.isNullOrEmpty() && !session.eventTemplateId.isNullOrEmpty()
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

    fun postAppLoaded() {
        if (_appState.value == false) {
            _appState.postValue(true)
        }
    }

    private fun postAppCleared() {
        if (_appState.value == true) {
            _appState.postValue(false)
        }
    }

    fun setAdminList(adminList: List<User> = emptyList()) {
        preferences.edit().putString(KEY_ADMIN_LIST, Gson().toJson(adminList)).commit()
        initializeSession()
    }

    fun setLoggedInUser(user: User?) {
        user?.let {
            preferences.edit().putString(KEY_SESSION_USER, Gson().toJson(user)).commit()
        }
        initializeSession()
    }

    fun setSessionToken(token: String?) {
        var _token = ""
        token?.let { _token = token }
        preferences.edit().putString(KEY_SESSION_TOKEN, _token).commit()
        initializeSession()
        postLoggedIn()
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
        postAppCleared()
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
        return session.eventTemplateId ?: ""
    }

    override fun getAnnouncementTemplateId(): String {
        return session.announcementTemplateId ?: ""
    }

    override fun getAdminList() : List<User> {
        return session.adminList ?: emptyList()
    }

    override fun isUserAdmin(): Boolean {
        return session.isAdmin
    }

    override fun isMe(user: User?): Boolean {
        return session.user?._id == user?._id
    }

    override fun isAdmin(user: User?): Boolean {
        val admins = getAdminList()
        for (admin in admins) {
            if (user?._id == admin._id) return true
        }
        return false
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
    var eventTemplateId: String? = null
    var announcementTemplateId: String? = null
    var adminList: List<User>? = null
}