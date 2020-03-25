
package com.locked.shingranicommunity.registration_login.registration.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.members.FragmentMemberViewModel
import com.locked.shingranicommunity.registration_login.registration.enterdetails.MAX_LENGTH
import com.locked.shingranicommunity.registration_login.registration.user.UserDataRepository
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject
import kotlin.math.log


class LoginViewModel @Inject constructor(private val userManager: UserManager):ViewModel() {

    @Inject
    lateinit var repository: UserDataRepository
    private val _loginState = MutableLiveData<LoginFormState>()
    val loginState: LiveData<LoginFormState>
        get() = _loginState
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult : LiveData<LoginResult>
        get() = _loginResult

    fun login(username: String, password: String) :LiveData<LoginFormState>{
        var loginSuccess : LiveData<LoginFormState>? = null
        if (userManager.loginUser()) {
            _loginState.value = LoginFormState(isDataValid = true)
        } else {
            loginSuccess = repository.login(username,password)
        }
        return loginSuccess!!
    }

    fun unregister() {
        userManager.unregister()
    }
    fun getUsername(): String = userManager.username
    fun getToken(): String = userManager.token
    fun validateInput(username: String, password: String) : Boolean{
        when {
            username.length < MAX_LENGTH ->{
                _loginState.value = LoginFormState(R.string.error_invalid_email)
                return true
            }
            username.equals(isUserNameValid(username)) ->{
                _loginState.value =  LoginFormState(R.string.error_invalid_email)
                return true
            }
            password.length < MAX_LENGTH -> {
                _loginState.value =  LoginFormState(R.string.error_invalid_email)
                return true
            }
            getToken().isBlank() ->{
                _loginState.value =  LoginFormState(R.string.error_invalid_email)
                return true
            }
            else -> {
//                login(username, password)
                LoginResult().success
                return false
            }
        }
    }
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginState.value = LoginFormState(isDataValid = true)
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun fetchedSingleApi() {
        repository.fetchedSingleApi()
    }
//    fun getAdminUser() = userManager.getAdminUser()
}
