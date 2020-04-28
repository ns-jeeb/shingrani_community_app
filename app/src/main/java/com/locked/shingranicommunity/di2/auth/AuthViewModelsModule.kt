package com.locked.shingranicommunity.di2.auth

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.navigation.AuthNavigation
import com.locked.shingranicommunity.auth.Navigation
import com.locked.shingranicommunity.auth.LoginViewModel
import com.locked.shingranicommunity.auth.RegisterViewModel
import com.locked.shingranicommunity.di2.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelsModule {

    /**
     * Any class that requires ViewModel with key LoginViewModel, then inject an instance of
     * LoginViewModel
     */
    @Binds
    @AuthScope
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel) : ViewModel

    /**
     * Any class that requires ViewModel with key RegisterViewModel, then inject an instance of
     * RegisterViewModel
     */
    @Binds
    @AuthScope
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel) : ViewModel

    @Binds
    @AuthScope
    abstract fun bindAuthNavigation(authNavigation: AuthNavigation) : Navigation
}