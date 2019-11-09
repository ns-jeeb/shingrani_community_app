package com.locked.shingranicommunity.authenticate.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.data.AuthenticationRepository
import com.locked.shingranicommunity.authenticate.data.LoginDataSource
import com.locked.shingranicommunity.authenticate.register.RegisterationViewModel

/**
 *
 *
 */
class LoginViewModelFactory(val loginEvent: LoginEvent) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                authenticationRepository = AuthenticationRepository(
                    loginEvent = loginEvent,
                    registerEvent = null
                )

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RegisterViewModelFactory(val authEvent: AuthenticationRepository.OnAuthenticatedSuccess) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterationViewModel::class.java)) {
            return RegisterationViewModel(
                authenticationRepository = AuthenticationRepository(
                    registerEvent = authEvent,
                    loginEvent = null

                )

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
