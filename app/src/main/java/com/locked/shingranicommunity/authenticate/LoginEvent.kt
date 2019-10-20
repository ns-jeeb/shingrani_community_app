package com.locked.shingranicommunity.authenticate

import com.locked.shingranicommunity.tutorials.User

interface LoginEvent {
    fun onLoginSuccess(user : User, isLogin: Boolean)
    fun onLoginFailed(error : Result<Any>)

}