package com.locked.shingranicommunity.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.BuildConfig
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val session: SessionManager,
    private val navigation: Navigation,
    private val res: ResourceProvider): ViewModel() {

    val title: LiveData<String>
    val email: LiveData<String>
    val fullName: LiveData<String>
    val appVersion: LiveData<String>

    init {
        title = MutableLiveData(res.getString(R.string.settings))
        email = MutableLiveData(session.getUsername())
        fullName = MutableLiveData(session.getFullName())
        appVersion = MutableLiveData(BuildConfig.VERSION_NAME)
    }

    fun logout() {
        session.logout()
        navigation.navigateToLogin()
    }
}