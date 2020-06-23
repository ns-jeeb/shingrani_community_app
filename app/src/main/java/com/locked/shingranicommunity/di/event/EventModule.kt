package com.locked.shingranicommunity.di.event

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.di.viewmodel.ViewModelKey
import com.locked.shingranicommunity.event.EventCreateViewModel
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
    abstract fun bindEventListViewModel(viewModel: EventListViewModel) : ViewModel

    /**
     * Any class that requires ViewModel with key EventCreateViewModel, then inject an instance of
     * EventCreateViewModel
     */
    @Binds
    @EventScope
    @IntoMap
    @ViewModelKey(EventCreateViewModel::class)
    abstract fun bindEventCreateViewModel(viewModel: EventCreateViewModel) : ViewModel

    @Binds
    @EventScope
    abstract fun bindNavigation(navigation: EventNavigation) : Navigation
}