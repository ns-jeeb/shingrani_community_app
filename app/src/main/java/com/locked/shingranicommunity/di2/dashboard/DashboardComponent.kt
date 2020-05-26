package com.locked.shingranicommunity.di2.dashboard

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.dashboard2.DashboardActivity
import dagger.BindsInstance
import dagger.Subcomponent

@DashboardScope
@Subcomponent(modules = [DashboardModule::class])
interface DashboardComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : DashboardComponent
    }

    fun inject(activity: DashboardActivity)
}