package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
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
}