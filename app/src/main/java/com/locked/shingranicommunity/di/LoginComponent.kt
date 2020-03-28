package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.di.scops.ActivityScope
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface LoginComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }
    fun inject(activity: LoginActivity)
}