package com.locked.shingranicommunity.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    val navigation: Navigation,
    val resourceProvider: ResourceProvider)
    : ViewModel() {

    private val data: Data = Data()
    val email: LiveData<String> = data.email
    val name: LiveData<String> = data.name
    val password: LiveData<String> = data.password
    val passwordConfirm: LiveData<String> = data.passwordConfirm
    val message: LiveData<String> = data.message
    val loading: LiveData<Boolean> = data.loading

    fun setEmail(email: String) { data.email.postValue(email) }
    fun setName(name: String) { data.name.postValue(name) }
    fun setPassword(password: String) { data.password.postValue(password) }
    fun setPasswordConfirm(passwordConfirm: String) { data.passwordConfirm.postValue(passwordConfirm) }
    fun onRegisterPress() {
        data.loading.postValue(true)
        data.message.postValue(resourceProvider.getString(R.string.please_wait))
        // todo
    }
    fun onLoginPress() {
        navigation.navigateToLogin(false)
    }

    class Data {
        val email: MutableLiveData<String> = MutableLiveData("")
        val name: MutableLiveData<String> = MutableLiveData("")
        val password: MutableLiveData<String> = MutableLiveData("")
        val passwordConfirm: MutableLiveData<String> = MutableLiveData("")
        val message: MutableLiveData<String> = MutableLiveData()
        val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    }
}
