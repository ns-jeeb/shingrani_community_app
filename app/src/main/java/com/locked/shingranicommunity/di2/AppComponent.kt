package com.locked.shingranicommunity.di2

import android.app.Application
import com.locked.shingranicommunity.dashboard2.DashboardActivity
import com.locked.shingranicommunity.di2.auth.AuthComponent
import com.locked.shingranicommunity.di2.dashboard.DashboardModule
import com.locked.shingranicommunity.di2.event.EventComponent
import com.locked.shingranicommunity.di2.locked.LockedApiServiceModule
import com.locked.shingranicommunity.di2.viewmodel.ViewModelFactoryModule
import com.locked.shingranicommunity.settings.SettingsActivity
import com.locked.shingranicommunity.storage.StorageModule
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    AppModule::class,
    ViewModelFactoryModule::class,
    LockedApiServiceModule::class,
    DashboardModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    val authComponentFactory : AuthComponent.Factory
    val eventComponentFactory : EventComponent.Factory

    fun inject(activity: DashboardActivity)
    fun inject(settingFragment: SettingsActivity.SettingFragment)
}