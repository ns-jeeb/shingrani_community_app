package com.locked.shingranicommunity.di.announcement

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.announcement.AnnouncementCreateViewModel
import com.locked.shingranicommunity.announcement.AnnouncementListViewModel
import com.locked.shingranicommunity.navigation.AnnouncementNavigation
import com.locked.shingranicommunity.announcement.Navigation
import com.locked.shingranicommunity.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AnnouncementModule {

    /**
     * Any class that requires ViewModel with key AnnouncementListViewModel, then inject an instance of
     * AnnouncementListViewModel
     */
    @Binds
    @AnnouncementScope
    @IntoMap
    @ViewModelKey(AnnouncementListViewModel::class)
    abstract fun bindAnnouncementListViewModel(viewModel: AnnouncementListViewModel) : ViewModel

    /**
     * Any class that requires ViewModel with key AnnouncementCreateViewModel, then inject an instance of
     * AnnouncementCreateViewModel
     */
    @Binds
    @AnnouncementScope
    @IntoMap
    @ViewModelKey(AnnouncementCreateViewModel::class)
    abstract fun bindAnnouncementCreateViewModel(viewModel: AnnouncementCreateViewModel) : ViewModel

    @Binds
    @AnnouncementScope
    abstract fun bindNavigation(navigation: AnnouncementNavigation) : Navigation
}