package com.locked.shingranicommunity.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    /**
     * Any class that requires ViewModelProvider.Factory, this function will inject an instance of
     * ViewModelProviderFactory.
     */
    @Binds
    abstract fun bindViewModelFactory(providerFactory: ViewModelProviderFactory) : ViewModelProvider.Factory
}