package com.locked.shingranicommunity.di2.event

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.dashboard2.DashboardActivity
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
}