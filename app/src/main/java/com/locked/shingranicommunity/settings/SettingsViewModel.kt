package com.locked.shingranicommunity.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val session: SessionManager): ViewModel() {

    val username: LiveData<String>
    val fullName: LiveData<String>

    init {
        username = MutableLiveData(session.getUsername())
        fullName = MutableLiveData(session.getFullName())
    }
}