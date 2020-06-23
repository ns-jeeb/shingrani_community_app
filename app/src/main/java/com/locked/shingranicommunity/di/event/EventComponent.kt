package com.locked.shingranicommunity.di.event

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.event.EventActivity
import com.locked.shingranicommunity.event.EventCreateFragment
import com.locked.shingranicommunity.event.EventListFragment
import dagger.BindsInstance
import dagger.Subcomponent

@EventScope
@Subcomponent(modules = [EventModule::class])
interface EventComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : EventComponent
    }

    fun inject(fragment: EventListFragment)
    fun inject(fragment: EventCreateFragment)
    fun inject(activity: EventActivity)
}