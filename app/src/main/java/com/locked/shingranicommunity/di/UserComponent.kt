package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.MainActivity
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import dagger.Subcomponent

@LoggedUserScope
@Subcomponent
interface UserComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): UserComponent
    }
    fun inject(activity: MainActivity)
    fun inject(activity: DashBoardViewPagerActivity)
}