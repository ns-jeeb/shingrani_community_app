package com.locked.shingranicommunity.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.repositories.UserRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
        val navigation: Navigation,
        val resourceProvider: ResourceProvider,
        private val userRepository: UserRepository)
    : ViewModel() {

    private val data: Data = Data()
    val email: LiveData<String> = data.email
    val name: LiveData<String> = data.name
    val password: LiveData<String> = data.password
    val passwordConfirm: LiveData<String> = data.passwordConfirm
    val message: LiveData<String> = data.message
    val loading: LiveData<Boolean> = data.loading

    private val registerStateObserver: Observer<UserRepository.RegisterData> = Observer<UserRepository.RegisterData> {
        it?.let {
            if (it.registerSuccess) {
                navigation.navigateToLogin(false)
            } else {
                data.loading.postValue(false)
                data.message.postValue(it.registerMessage)
            }
        }
    }
    init {
        userRepository.registerState.observeForever(registerStateObserver)
    }

    fun setEmail(email: String) {
        data.email.postValue(email)
    }
    fun setName(name: String) {
        data.name.postValue(name)
    }
    fun setPassword(password: String) {
        data.password.postValue(password)
    }
    fun setPasswordConfirm(passwordConfirm: String) {
        data.passwordConfirm.postValue(passwordConfirm)
    }
    fun onRegisterPress() {
        when {
            validateName(data.name.value!!) && validateEmail(data.email.value!!) && validatePassword(data.password.value!!, data.passwordConfirm.value!!) -> {
                data.email.value?.let { email -> data.password.value?.let { password -> data.name.value?.let { name -> userRepository.register(email, password, name) } } }
            }
            else -> {
                data.loading.postValue(false)
            }
        }
    }

    private fun validateEmail(email: String): Boolean{
        return if (email.isNullOrBlank()){
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
            data.message.postValue(resourceProvider.getString(R.string.error_invalid_email))
            false
        }else email.contains("@")
    }
    private fun validatePassword(password: String, conformPassword: String):Boolean{
        return (when {
            password.isNullOrBlank() -> {
                data.message.postValue(resourceProvider.getString(R.string.error_incorrect_password))
                false
            }
            conformPassword.isNullOrBlank() ->{
                data.message.postValue(resourceProvider.getString(R.string.confirm_incorrect_password))
                false
            }
            password != conformPassword -> {
                data.message.postValue(resourceProvider.getString(R.string.not_match_password))
                false
            }
            password.length < 6 -> {
                data.message.postValue(resourceProvider.getString(R.string.invalid_password))
                false
            }
            else -> {
                true
            }
        })
    }
    private fun validateName(name: String):Boolean{
        return if (name.isBlank() || name.length < 2){
            data.message.postValue(resourceProvider.getString(R.string.invalid_username))
            false
        }else{
            data.name.postValue(name)
            true
        }
    }
    override fun onCleared() {
        super.onCleared()
        userRepository.registerState.removeObserver(registerStateObserver)
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

    fun messageHandled() {
        data.message.value = null
    }
}
