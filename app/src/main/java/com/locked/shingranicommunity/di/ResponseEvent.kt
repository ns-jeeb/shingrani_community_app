package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity


interface ResponseEvent {
    fun loggedInSuccess(username:String, activity: LoginActivity)
    fun failedLoggedIn(error: String)
    fun userCreated(username:String,message:String)
    fun failedCreateUser(username:String,error:String)
    fun itemCreatedMessageDisplay()
}