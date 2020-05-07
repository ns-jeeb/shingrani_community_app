package com.locked.shingranicommunity.event

import android.os.Bundle
import com.locked.shingranicommunity.MyApplication
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.di2.event.EventComponent

class EventActivity : FragmentActivity(), EventComponentProvider {

    override lateinit var eventComponent: EventComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        eventComponent = MyApplication.instance.appComponent2.eventComponentFactory.create(this)
        eventComponent.inject(this)
        super.onCreate(savedInstanceState)
    }
}