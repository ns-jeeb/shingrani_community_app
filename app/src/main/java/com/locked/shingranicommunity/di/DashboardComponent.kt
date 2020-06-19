package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.dashboard.announncement.AnnounceFragment
import com.locked.shingranicommunity.dashboard.announncement.create_announce.CreateAnnouncementActivity
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventListFragment
import com.locked.shingranicommunity.details.DetailsActivity
import com.locked.shingranicommunity.details.DetailsFragment
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
    fun inject(activity: CreateAnnouncementActivity)
    fun inject(activity: DetailsActivity)
    fun inject(fragment: EventListFragment)
    fun inject(fragment: AnnounceFragment)
    fun inject(fragment: DetailsFragment)
}