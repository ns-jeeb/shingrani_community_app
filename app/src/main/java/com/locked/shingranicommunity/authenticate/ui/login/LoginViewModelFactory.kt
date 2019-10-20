package com.locked.shingranicommunity.authenticate.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.data.LoginDataSource
import com.locked.shingranicommunity.authenticate.data.LoginRepository
import kotlin.coroutines.coroutineContext

/**
 *
 *
 */
class LoginViewModelFactory(private val loginEvent: LoginEvent) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(
                        loginEvent = loginEvent
                    )
                )

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
