package com.locked.shingranicommunity.di

import android.content.Context
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import com.locked.shingranicommunity.storage.StorageModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, CommAppSubComponent::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun registerComponent(): RegisterComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun userManager(): UserManager

    fun inject(activity: LoginActivity)
}