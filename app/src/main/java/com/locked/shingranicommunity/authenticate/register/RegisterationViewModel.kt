package com.locked.shingranicommunity.authenticate.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.authenticate.ui.login.AuthenticFormState
import com.locked.shingranicommunity.authenticate.ui.login.LoginResult

class RegisterationViewModel : ViewModel() {
    private val _registerForm = MutableLiveData<AuthenticFormState>()
    val authenticFormState: LiveData<AuthenticFormState> = _registerForm
    private val _registerResult = MutableLiveData<LoginResult>()
    val registerResult: LiveData<LoginResult> = _registerResult
}
