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
import com.locked.shingranicommunity.authenticate.data.LoginRepository
import com.locked.shingranicommunity.authenticate.data.Result
import com.locked.shingranicommunity.tutorials.User
import com.locked.shingranicommunity.tutorials.UserDao
import com.locked.shingranicommunity.tutorials.UserDatabase
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel(),LoginEvent{
    override fun onLoginSuccess(user: User, isLogin: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoginFailed(error: kotlin.Result<Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase

//    val userId : String = savedStateHandle["uid"] ?:
//    throw IllegalArgumentException("missing user id")


    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        if (result is Result.Success<*>) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.toString(), userName = result.data.toString() ,userId = result.data.toString()
            ))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
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
    fun auth(){
        var users : ArrayList<User> = userDao.getAll() as ArrayList<User>
        for (user in users){
            Toast.makeText(CommunityApp.instance,"this is my tost of user",Toast.LENGTH_LONG).show()
        }
    }
}
