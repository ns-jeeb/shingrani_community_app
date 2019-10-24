package com.locked.shingranicommunity.authenticate

interface LoginEvent {
    fun onLoginSuccess(): Boolean
    fun onLoginFailed(error : Result<Any>)

}