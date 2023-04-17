package com.locked.shingranicommunity.di.member

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.member.InviteMemberFragment
import com.locked.shingranicommunity.member.MemberActivity
import com.locked.shingranicommunity.member.MemberListFragment
import dagger.BindsInstance
import dagger.Subcomponent

@MemberScope
@Subcomponent(modules = [MemberModule::class])
interface MemberComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : MemberComponent
    }

    fun inject(activity: MemberActivity)
    fun inject(fragment: InviteMemberFragment)
    fun inject(fragment: MemberListFragment)
}