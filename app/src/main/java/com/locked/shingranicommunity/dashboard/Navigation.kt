package com.locked.shingranicommunity.dashboard

interface Navigation {

    fun navigateToCreateEvent(addToBackStack: Boolean)
    fun navigateToCreateAnnouncement(addToBackStack: Boolean)
    fun navigateToMemberList(addToBackStack: Boolean)
    fun navigateToSettings(addToBackStack: Boolean)
}