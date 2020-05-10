package com.locked.shingranicommunity.di2.event

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.di2.viewmodel.ViewModelKey
import com.locked.shingranicommunity.event.EventListViewModel
import com.locked.shingranicommunity.event.Navigation
import com.locked.shingranicommunity.navigation.EventNavigation
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EventModule {

    /**
     * Any class that requires ViewModel with key EventListViewModel, then inject an instance of
     * EventListViewModel
     */
    @Binds
    @EventScope
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    abstract fun bindLoginViewModel(viewModel: EventListViewModel) : ViewModel

    @Binds
    @EventScope
    abstract fun bindNavigation(navigation: EventNavigation) : Navigation
}