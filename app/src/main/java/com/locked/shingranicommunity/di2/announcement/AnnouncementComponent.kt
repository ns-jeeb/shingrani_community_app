package com.locked.shingranicommunity.di2.announcement

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.announcement.AnnouncementActivity
import com.locked.shingranicommunity.announcement.AnnouncementCreateFragment
import com.locked.shingranicommunity.announcement.AnnouncementListFragment
import dagger.BindsInstance
import dagger.Subcomponent

@AnnouncementScope
@Subcomponent(modules = [AnnouncementModule::class])
interface AnnouncementComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : AnnouncementComponent
    }

    fun inject(activity: AnnouncementActivity)
    fun inject(fragment: AnnouncementListFragment)
    fun inject(fragment: AnnouncementCreateFragment)
}