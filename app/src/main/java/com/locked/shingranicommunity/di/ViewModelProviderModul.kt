package com.locked.shingranicommunity.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.di2.viewmodel.ViewModelKey
import com.locked.shingranicommunity.di2.viewmodel.ViewModelProviderFactory
import com.locked.shingranicommunity.dashboard.DashBoardViewModel
import com.locked.shingranicommunity.dashboard.announncement.AnnounceViewModel
import com.locked.shingranicommunity.dashboard.announncement.create_announce.CreateAnnounceViewModel
import com.locked.shingranicommunity.dashboard.event.details.DetailsViewModel
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventViewModel
import com.locked.shingranicommunity.members.FragmentMemberViewModel
import com.locked.shingranicommunity.members.MemberViewModel
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
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(loginViewModel: LoginViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun registerViewModel(regiViewModel: RegistrationViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashBoardViewModel::class)
    abstract fun dashboarViewModel(dashboardViewModel:DashBoardViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun eventViewModel(eventViewModel: EventViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnnounceViewModel::class)
    abstract fun announcementViewModel(announceViewModel: AnnounceViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MemberViewModel::class)
    abstract fun memberViewModel(memberViewModel: MemberViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentMemberViewModel::class)
    abstract fun fragmentMemberViewModel(fragmenMemberViewModel: FragmentMemberViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAnnounceViewModel::class)
    abstract fun createAnnounceViewModel(createAnnounceViewModel: CreateAnnounceViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun detailsViewModel(detailsViewModel: DetailsViewModel):ViewModel

}