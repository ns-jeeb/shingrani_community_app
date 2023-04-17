package com.locked.shingranicommunity.di.settings

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.di.viewmodel.ViewModelKey
import com.locked.shingranicommunity.navigation.SettingsNavigation
import com.locked.shingranicommunity.settings.Navigation
import com.locked.shingranicommunity.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    /**
     * Any class that requires ViewModel with key SettingsViewModel, then inject an instance of
     * SettingsViewModel
     */
    @Binds
    @SettingsScope
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel) : ViewModel

    @Binds
    @SettingsScope
    abstract fun bindNavigation(navigation: SettingsNavigation) : Navigation
}