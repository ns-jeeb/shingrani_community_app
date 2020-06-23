package com.locked.shingranicommunity.event

import com.locked.shingranicommunity.locked.models.EventItem

interface Navigation {
    fun navigateToMap(address: String)
    fun navigateShare(data: EventItem)
    fun navigateToEventDetail(eventId: String)
    fun createFinished()
    fun navigateSearchAddress(addToBackStack: Boolean)
    fun navigateToLogin(clearSession: Boolean)
}