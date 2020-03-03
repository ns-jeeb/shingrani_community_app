package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.members.MemberActivity
import dagger.Subcomponent

@Subcomponent
interface MemberComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): MemberComponent
    }

    fun inject(activity: MemberActivity)
}