package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.locked.shingranicommunity.di2.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.LoginRequestBody
import com.locked.shingranicommunity.models.Error
import com.locked.shingranicommunity.models.ErrorMessage
import com.locked.shingranicommunity.models.LoginResponse
import com.locked.shingranicommunity.models.User
import com.locked.shingranicommunity.session.Session
import com.locked.shingranicommunity.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AppScope
class UserRepository @Inject constructor(
    val apiService: LockedApiService,
    val sessionManager: SessionManager) {

    private val _loginData = MutableLiveData<LoginData>()
    var loginState: LiveData<LoginData> = _loginData

    init {
        if (sessionManager.isLoggedIn()) {
            _loginData.postValue(LoginData(true))
        }
    }

    fun login(username: String, password: String) {
        val call: Call<LoginResponse> = apiService.login(LoginRequestBody(username, password))
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

    fun register() {
        // todo
    }

    data class LoginData(var loggedInSuccess: Boolean = false,
                         var loginMessage: String? = null)
}