package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.di.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.Error
import com.locked.shingranicommunity.locked.models.request.LoginRequestBody
import com.locked.shingranicommunity.locked.models.request.RegisterRequestBody
import com.locked.shingranicommunity.locked.models.response.LoginResponse
import com.locked.shingranicommunity.locked.models.response.RegisterResponse
import com.locked.shingranicommunity.session.SessionManager
import retrofit2.Call
import javax.inject.Inject

@AppScope
class UserRepository @Inject constructor(
    val apiService: LockedApiService,
    val sessionManager: SessionManager) {

    private val _loginData = MutableLiveData<LoginData>()
    var loginState: LiveData<LoginData> = _loginData

    private val _registerData = MutableLiveData<RegisterData>()
    var registerState: LiveData<RegisterData> = _registerData

    init {
        if (sessionManager.isLoggedIn()) {
            _loginData.value = LoginData(true)
        }
        sessionManager.logoutEvent.observeForever(object: Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    _loginData.value = LoginData(false)
                }
            }
        })
    }

    fun login(username: String, password: String) {
        val call: Call<LoginResponse> = apiService.login(
            LoginRequestBody(
                username,
                password
            )
        )
        call.enqueue(object: LockedCallback<LoginResponse>() {
            override fun success(response: LoginResponse) {
                sessionManager.setLoggedInUser(response.user)
                sessionManager.setSessionToken(response.token)
                _loginData.postValue(LoginData(true, response.message))
            }
            override fun fail(message: String, details: List<Error>) {
                sessionManager.setLoggedInUser(null)
                sessionManager.setSessionToken(null)
                _loginData.postValue(LoginData(false, message))
            }
        })
    }

    fun register(username: String,password: String,name: String) {
        val call = apiService.register(
            RegisterRequestBody(
                name,
                password,
                username
            )
        )
        call.enqueue(object: LockedCallback<RegisterResponse>() {
            override fun success(response: RegisterResponse) {
                _registerData.postValue(RegisterData(true, response.message))
            }
            override fun fail(message: String, details: List<Error>) {
                _registerData.postValue(RegisterData(false, message))
            }
        })
    }

    data class LoginData(var loggedInSuccess: Boolean = false,
                         var loginMessage: String? = null)
    data class RegisterData(var registerSuccess: Boolean = false,
                         var registerMessage: String? = null)
}