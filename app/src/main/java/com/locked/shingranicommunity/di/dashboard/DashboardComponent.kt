package com.locked.shingranicommunity.di.dashboard

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.dashboard.DashboardActivity
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