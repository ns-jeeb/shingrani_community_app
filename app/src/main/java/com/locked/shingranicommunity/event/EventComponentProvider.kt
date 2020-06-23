package com.locked.shingranicommunity.event

import com.locked.shingranicommunity.di.event.EventComponent

interface EventComponentProvider {
    var eventComponent: EventComponent
}