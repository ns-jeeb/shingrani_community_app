
package com.locked.shingranicommunity.registration_login.registration.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.registration_login.registration.enterdetails.EnterDetailsError
import com.locked.shingranicommunity.registration_login.registration.enterdetails.EnterDetailsSuccess
import com.locked.shingranicommunity.registration_login.registration.enterdetails.MAX_LENGTH
//import com.locked.shingranicommunity.authenticate.LoginEvent
//import com.locked.shingranicommunity.authenticate.data.Result
//import com.locked.shingranicommunity.authenticate.ui.login.AuthenticFormState
//import com.locked.shingranicommunity.authenticate.ui.login.LoggedInUserView
//import com.locked.shingranicommunity.authenticate.ui.login.LoginResult
import com.locked.shingranicommunity.registration_login.registration.user.UserDataRepository
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

/**
 * LoginViewModel is the ViewModel that [LoginActivity] uses to
 * obtain information of what to show on the screen and handle complex logic.
 */

class LoginViewModel @Inject constructor(private val userManager: UserManager) {

    @Inject
    lateinit var repository: UserDataRepository
    private val _loginState = MutableLiveData<LoginViewState>()
    val loginState: LiveData<LoginViewState>
        get() = _loginState

    fun login(username: String, password: String) {
        if (userManager.loginUser()) {
            _loginState.value = LoginSuccess
        } else {
            validateInput(username,password)
            repository.login(username,password)
        }
    }

    fun unregister() {
        userManager.unregister()
    }

    fun getUsername(): String = userManager.username
    fun getToken(): String = userManager.token


    fun validateInput(username: String, password: String) : Boolean{
        when {
            username.length < MAX_LENGTH ->{
                _loginState.value = LoginError("Username has to be longer than 4 characters")
                return true
            }
            username.equals(isUserNameValid(username)) ->{
                _loginState.value = LoginError("User name is invalid")
                return true
            }

            password.length < MAX_LENGTH -> {
                _loginState.value = LoginError("Password has to be longer than 4 characters")
                return true
            }
            getToken().isBlank() ->{
                _loginState.value = LoginError("No User")
                return true
            }
            else -> {
                _loginState.value = LoginSuccess
                return false
            }
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
}
