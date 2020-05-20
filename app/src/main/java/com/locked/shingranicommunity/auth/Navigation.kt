package com.locked.shingranicommunity.auth

interface Navigation {
    fun navigateToLogin(addToBackStack: Boolean)
    fun navigateToRegister(addToBackStack: Boolean)
    fun navigateToNext()
}