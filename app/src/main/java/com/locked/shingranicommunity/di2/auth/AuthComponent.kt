package com.locked.shingranicommunity.di2.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.auth.RegisterFragment
import dagger.BindsInstance
import dagger.Subcomponent

@AuthScope
@Subcomponent(modules = [AuthViewModelsModule::class])
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : AuthComponent
    }

    fun inject(authActivity: AuthActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
}