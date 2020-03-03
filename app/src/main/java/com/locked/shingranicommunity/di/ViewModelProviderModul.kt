package com.locked.shingranicommunity.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.dashboard.DashBoardViewModel
import com.locked.shingranicommunity.dashboard.announncement.AnnounceViewModel
import com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent.CreateEventViewModel
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventViewModel
import com.locked.shingranicommunity.members.MemberViewModel
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.registration_login.registration.RegistrationViewModel
import com.locked.shingranicommunity.registration_login.registration.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelProviderModul {
    @Binds
    abstract fun bindViewModel(viewModeProvider: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(loginViewModel: LoginViewModel):ViewModel

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(RegistrationViewModel::class)
    abstract fun registerViewModel(regiViewModel: RegistrationViewModel):ViewModel

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(DashBoardViewModel::class)
    abstract fun dashboarViewModel(dashboardViewModel:DashBoardViewModel):ViewModel

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(EventViewModel::class)
    abstract fun eventViewModel(eventViewModel: EventViewModel):ViewModel

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(CreateEventViewModel::class)
    abstract fun createEventViewModel(createEventViewModel: CreateEventViewModel):ViewModel

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(AnnounceViewModel::class)
    abstract fun announcementViewModel(announceViewModel: AnnounceViewModel):ViewModel

    @Binds
    @IntoMap
    @MyApplication.ViewModelKey(MemberViewModel::class)
    abstract fun memberViewModel(memberViewModel: MemberViewModel):ViewModel

}