package com.locked.shingranicommunity.event

import com.locked.shingranicommunity.models.EventItem

interface Navigation {
    fun navigateToMap(address: String)
    fun navigateShare(data: EventItem)
    fun navigateToEventDetails(eventId: String)
}