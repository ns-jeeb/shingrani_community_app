package com.locked.shingranicommunity.authenticate.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.data.AuthenticationRepository
import com.locked.shingranicommunity.authenticate.data.Result
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import com.locked.shingranicommunity.authenticate.ui.login.AuthenticFormState
import com.locked.shingranicommunity.authenticate.ui.login.LoggedInUserView
import com.locked.shingranicommunity.authenticate.ui.login.LoginResult
import javax.inject.Inject

class RegisterationViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) : ViewModel() {


    private val _registerForm = MutableLiveData<AuthenticFormState>()
    val authenticFormState: LiveData<AuthenticFormState> = _registerForm
    private val _registerResult = MutableLiveData<LoginResult>()
    val registerResult: LiveData<LoginResult> = _registerResult

    fun onRegisterSuccess(onSuccessRegister: AuthenticationRepository.OnRegisterSuccess){
        authenticationRepository.setOnClickListener(onSuccessRegister)
    }

    fun register(name:String,username: String,password:String, conPass: String){
        var result: Result<LoggedInUser>? = null

        result = authenticationRepository.register(username,password,name)
        if (result is Result.Success<*>) {
            _registerResult.value = LoginResult(success = LoggedInUserView(displayName = name, userName = username ,userId = result.data.toString()))
        } else {
            _registerResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun registerDataChanged(username: String, password: String,conPass: String) {

        if (!isUserNameValid(username)) {
            _registerForm.value = AuthenticFormState(usernameError = R.string.invalid_username)
        }
        else if (!isPasswordValid(password)) {
            _registerForm.value = AuthenticFormState(passwordError = R.string.invalid_password)
        }
        else if (!isConformPasswordValid(password, conPass)){
            _registerForm.value = AuthenticFormState(passwordConformError = R.string.not_match_password)

        }else{
            _registerForm.value = AuthenticFormState(isDataValid =  true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (!username.contains('@')|| !username.contains('.')&& username.isNotEmpty()) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length>5
    }

    private fun isConformPasswordValid(password: String,passwordConform: String): Boolean {
        return password.contentEquals(passwordConform)

    }
}
