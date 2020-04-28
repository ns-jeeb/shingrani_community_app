package com.locked.shingranicommunity.di2

import android.app.Application
import com.locked.shingranicommunity.di2.auth.AuthComponent
import com.locked.shingranicommunity.di2.locked.LockedApiServiceModule
import com.locked.shingranicommunity.di2.viewmodel.ViewModelFactoryModule
import com.locked.shingranicommunity.storage.StorageModule
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [AppModule::class,
    ViewModelFactoryModule::class,
    LockedApiServiceModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    val authComponentFactory : AuthComponent.Factory
}