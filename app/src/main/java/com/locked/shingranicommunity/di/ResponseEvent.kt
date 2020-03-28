package com.locked.shingranicommunity.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import com.locked.shingranicommunity.registration_login.registration.login.LoginFormState


interface ResponseEvent {
    fun loggedInSuccess(username:String, activity: LoginActivity)
    fun failedLoggedIn(error: String)
    fun userCreated(username:String,password:String, name:String): LiveData<LoginFormState>
    fun failedCreateUser(username:String,error:String)
    fun itemCreatedMessageDisplay()
    fun fetchedSingleApi()
}