package com.locked.shingranicommunity.di


interface ResponseEvent {
    fun loggedInSuccess(username:String)
    fun failedLoggedIn(error: String)
    fun userCreated(username:String,message:String)
    fun failedCreateUser(username:String,error:String)
    fun itemCreatedMessageDisplay()
}