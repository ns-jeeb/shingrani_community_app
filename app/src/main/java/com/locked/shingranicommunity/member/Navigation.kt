package com.locked.shingranicommunity.member

interface Navigation {

    fun navigateToInvite(addToBackStack: Boolean)
    fun navigateToLogin(clearSession: Boolean)
    fun sendEmail(email: String)
    fun inviteFinished()
}