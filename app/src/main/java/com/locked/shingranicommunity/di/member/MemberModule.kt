package com.locked.shingranicommunity.di.member

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.di.viewmodel.ViewModelKey
import com.locked.shingranicommunity.member.InviteViewModel
import com.locked.shingranicommunity.member.MemberListViewModel
import com.locked.shingranicommunity.member.Navigation
import com.locked.shingranicommunity.navigation.MemberNavigation
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MemberModule {

    /**
     * Any class that requires ViewModel with key MemberListViewModel, then inject an instance of
     * MemberListViewModel
     */
    @Binds
    @MemberScope
    @IntoMap
    @ViewModelKey(MemberListViewModel::class)
    abstract fun bindLoginViewModel(viewModel: MemberListViewModel) : ViewModel

    /**
     * Any class that requires ViewModel with key InviteViewModel, then inject an instance of
     * InviteViewModel
     */
    @Binds
    @MemberScope
    @IntoMap
    @ViewModelKey(InviteViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: InviteViewModel) : ViewModel

    @Binds
    @MemberScope
    abstract fun bindAuthNavigation(memberNavigation: MemberNavigation) : Navigation
}