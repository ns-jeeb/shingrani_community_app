package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.dashboard.announncement.AnnounceFragment
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent.CreateEventFragment
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventListFragment
import com.locked.shingranicommunity.di.scops.LoggedUserScope
import dagger.Subcomponent

@LoggedUserScope
@Subcomponent
interface DashboardComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): DashboardComponent
    }
    fun inject(activity: DashBoardViewPagerActivity)
    fun inject(activity: CreateItemActivity)
    fun inject(fragment: EventListFragment)
    fun inject(fragment: AnnounceFragment)
    fun inject(fragment: CreateEventFragment)
//    fun inject(fragment: AnnounceFragment)
}