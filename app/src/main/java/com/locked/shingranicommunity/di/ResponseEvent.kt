package com.locked.shingranicommunity.di


interface ResponseEvent {
    fun loginMessageDisplay()
    fun registerMessageDisplay(username:String)
    fun itemCreatedMessageDisplay()
}