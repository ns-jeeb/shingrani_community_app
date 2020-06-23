package com.locked.shingranicommunity.di

import android.app.Application
import com.locked.shingranicommunity.di.announcement.AnnouncementComponent
import com.locked.shingranicommunity.di.auth.AuthComponent
import com.locked.shingranicommunity.di.dashboard.DashboardComponent
import com.locked.shingranicommunity.di.event.EventComponent
import com.locked.shingranicommunity.di.locked.LockedApiServiceModule
import com.locked.shingranicommunity.di.member.MemberComponent
import com.locked.shingranicommunity.di.viewmodel.ViewModelFactoryModule
import com.locked.shingranicommunity.di2.item_details.ItemDetailsComponent
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
    val memberComponentFactory : MemberComponent.Factory
    val itemDetailsComponent : ItemDetailsComponent.Factory

    fun inject(settingFragment: SettingsActivity.SettingFragment)
}