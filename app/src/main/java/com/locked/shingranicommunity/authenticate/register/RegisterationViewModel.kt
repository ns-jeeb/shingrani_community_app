package com.locked.shingranicommunity.authenticate.register

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

    fun register(name:String,username: String,password:String){
        var result: Result<LoggedInUser>? = null

        result = authenticationRepository.register(username,password,name)
        if (result is Result.Success<*>) {
            _registerResult.value = LoginResult(success = LoggedInUserView(displayName = name, userName = username ,userId = result.data.toString()))
        } else {
            _registerResult.value = LoginResult(error = R.string.login_failed)
        }
    }

}
