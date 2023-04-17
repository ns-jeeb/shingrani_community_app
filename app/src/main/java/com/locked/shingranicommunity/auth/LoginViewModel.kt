
package com.locked.shingranicommunity.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.repositories.AppRepository
import com.locked.shingranicommunity.repositories.UserRepository
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val navigation: Navigation,
    private val resourceProvider: ResourceProvider,
    private val userRepository: UserRepository,
    private val appRepository: AppRepository,
    private val session: Session) : ViewModel() {

    private val data: Data = Data()
    val email: LiveData<String> = data.email
    val password: LiveData<String> = data.password
    val message: LiveData<String> = data.message
    val loading: LiveData<Boolean> = data.loading

    private val loginStateObserver: Observer<UserRepository.LoginData> = Observer<UserRepository.LoginData> {
        it?.let {
            if (it.loggedInSuccess) {
                appRepository.fetchApp()
            } else {
                data.loading.postValue(false)
                data.message.postValue(it.loginMessage)
            }
        }
    }
    private val appStateObserver: Observer<AppRepository.Data> = Observer<AppRepository.Data> {
        it?.let {
            data.loading.postValue(false)
            if (it.success && session.isLoggedIn()) {
                data.message.postValue(resourceProvider.getString(R.string.welcome_user).format(session.getFullName()))
                navigation.navigateToNext()
            } else {
                data.message.postValue(it.message)
            }
        }
    }

    init {
        userRepository.loginState.observeForever(loginStateObserver)
        appRepository.fetchApp.observeForever(appStateObserver)
        data.loading.value = true
    }

    fun setEmail(email: String) { data.email.value = email }
    fun setPassword(password: String) { data.password.value = password }
    fun onLoginPress() {
        data.loading.postValue(true)
        userRepository.login(email.value!!, password.value!!)
    }
    fun onRegisterPress() {
        navigation.navigateToRegister(true)
    }

    class Data {
        val email: MutableLiveData<String> = MutableLiveData("")
        val password: MutableLiveData<String> = MutableLiveData("")
        val message: MutableLiveData<String> = MutableLiveData()
        val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.loginState.removeObserver(loginStateObserver)
        appRepository.fetchApp.removeObserver(appStateObserver)
    }

    fun messageHandled() {
        data.message.value = null
    }
}

