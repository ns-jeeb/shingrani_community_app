package com.locked.shingranicommunity.authenticate

interface LoginEvent {
    fun onLoginSuccess(isTokenValid: Boolean)
    fun onLoginFailed(error : String)

}