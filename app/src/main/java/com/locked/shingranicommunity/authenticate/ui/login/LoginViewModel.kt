package com.locked.shingranicommunity.authenticate.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.data.AuthenticationRepository
import com.locked.shingranicommunity.authenticate.data.Result
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import com.locked.shingranicommunity.tutorials.UserDao
import com.locked.shingranicommunity.tutorials.UserDatabase
import javax.inject.Inject
import kotlin.math.log

class LoginViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) : ViewModel(){
//    override fun onLoginSuccess():Boolean {
//        return true
//    }
//    override fun onLoginFailed(error: String) {
//
//        if (error.isNotEmpty()){
//            Toast.makeText(CommunityApp.instance,"Failed login",Toast.LENGTH_LONG).show()
//        }
//    }

    lateinit var  _loginEvent: LiveData<LoginEvent>


    private val _loginForm = MutableLiveData<AuthenticFormState>()
    val authenticFormState: LiveData<AuthenticFormState> = _loginForm
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private var userDao: UserDao? = null
    private lateinit var db: UserDatabase

//    val userId : String = savedStateHandle["uid"] ?:
//    throw IllegalArgumentException("missing user id")
    fun onLoginEvent(loginEvent: LoginEvent) {
        authenticationRepository.setOnLoginEvent(loginEvent)
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        var result: Result<LoggedInUser>? = null
        var sharedPreferences = CommunityApp.instance.getSharedPreferences("token",Context.MODE_PRIVATE)
        if (sharedPreferences.getString("token","") !== null && sharedPreferences.getString("token","")!==""){
            result = authenticationRepository.getToken(sharedPreferences)
        }else{
            result = authenticationRepository.login(username, password)
        }

        if (result is Result.Success<*>) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = username, userName = username ,userId = result.data.toString()
            ))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = AuthenticFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = AuthenticFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = AuthenticFormState(isDataValid = true)
        }
    }

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
