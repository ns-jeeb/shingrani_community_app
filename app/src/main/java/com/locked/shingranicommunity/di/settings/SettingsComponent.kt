package com.locked.shingranicommunity.di.settings

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.settings.SettingsActivity
import dagger.BindsInstance
import dagger.Subcomponent

@SettingsScope
@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : SettingsComponent
    }

    fun inject(activity: SettingsActivity)
    fun inject(fragment: SettingsActivity.SettingFragment)
}