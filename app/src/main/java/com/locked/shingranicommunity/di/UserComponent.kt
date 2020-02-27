package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.MainActivity
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.di.scops.LoggedUserScope
import com.locked.shingranicommunity.storage.SharedPreferencesStorage
import dagger.Binds
import dagger.Subcomponent

@LoggedUserScope
@Subcomponent
interface UserComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): UserComponent
    }
}