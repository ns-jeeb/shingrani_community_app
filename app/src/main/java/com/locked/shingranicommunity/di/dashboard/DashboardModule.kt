package com.locked.shingranicommunity.di.dashboard

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.dashboard.DashboardViewModel
import com.locked.shingranicommunity.dashboard.Navigation
import com.locked.shingranicommunity.di.viewmodel.ViewModelKey
import com.locked.shingranicommunity.navigation.DashboardNavigation
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DashboardModule {

    /**
     * Any class that requires ViewModel with key DashboardViewModel, then inject an instance of
     * DashboardViewModel
     */
    @Binds
    @DashboardScope
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel) : ViewModel

    @Binds
    @DashboardScope
    abstract fun bindNavigation(navigation: DashboardNavigation) : Navigation
}