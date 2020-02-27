package com.locked.shingranicommunity.di

interface Repository {
    fun login()
    fun registerUser()
    fun loadingItem()
    fun loadingEvent()
    fun loadingAnnouncement()
    fun createEvent()
    fun createAnnouncement()
}