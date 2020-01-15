
package com.locked.shingranicommunity.registration_login.registration.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
            _loginState.value = LoginError
            repository.login(username,password)
        }
    }

    fun unregister() {
        userManager.unregister()
    }

    fun getUsername(): String = userManager.username




//    lateinit var  _loginEvent: LiveData<LoginEvent>


//    private val _loginForm = MutableLiveData<AuthenticFormState>()
//    val authenticFormState: LiveData<AuthenticFormState> = _loginForm
//    private val _loginResult = MutableLiveData<LoginResult>()
//    val loginResult: LiveData<LoginResult> = _loginResult
//
//    private var userDao: UserDao? = null
//    private lateinit var db: UserDatabase

    //    val userId : String = savedStateHandle["uid"] ?:
//    throw IllegalArgumentException("missing user id")
//    fun onLoginEvent(loginEvent: LoginEvent) {
//        authenticationRepository.setOnLoginEvent(loginEvent)
//    }

//    fun login(username: String, password: String) {
//        // can be launched in a separate asynchronous job
//        var result: Result<LoggedInUser>? = null
//        var sharedPreferences = MyApplication.instance.getSharedPreferences("token", Context.MODE_PRIVATE)
//
//        if (sharedPreferences.getString("token","") === null || sharedPreferences.getString("token","") === "") {
//            result = authenticationRepository.login(username, password)
//        } else {
//            result = authenticationRepository.getToken(sharedPreferences)
//        }
//
//        if (result is Result.Success<*>) {
//            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = username, userName = username ,userId = result.data.toString()
//            )
//            )
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
//    }


    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5;
    }
}
