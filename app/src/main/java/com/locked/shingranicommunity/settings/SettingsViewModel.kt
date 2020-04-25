package com.locked.shingranicommunity.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val userManager: UserManager): ViewModel() {

    val username: LiveData<String>
    val user_fullName: LiveData<String>

    init {
        username = MutableLiveData<String>(userManager.getCurrentUser()?.username)
        user_fullName = MutableLiveData<String>(userManager.getCurrentUser()?.name)
    }
}