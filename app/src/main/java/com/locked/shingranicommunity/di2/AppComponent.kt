package com.locked.shingranicommunity.di2

import android.app.Application
import com.locked.shingranicommunity.di2.announcement.AnnouncementComponent
import com.locked.shingranicommunity.di2.auth.AuthComponent
import com.locked.shingranicommunity.di2.dashboard.DashboardComponent
import com.locked.shingranicommunity.di2.event.EventComponent
import com.locked.shingranicommunity.di2.locked.LockedApiServiceModule
import com.locked.shingranicommunity.di2.viewmodel.ViewModelFactoryModule
import com.locked.shingranicommunity.settings.SettingsActivity
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    AppModule::class,
    ViewModelFactoryModule::class,
    LockedApiServiceModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    val authComponentFactory : AuthComponent.Factory
    val dashboardComponentFactory : DashboardComponent.Factory
    val eventComponentFactory : EventComponent.Factory
    val announcementComponentFactory : AnnouncementComponent.Factory

    fun inject(settingFragment: SettingsActivity.SettingFragment)
}