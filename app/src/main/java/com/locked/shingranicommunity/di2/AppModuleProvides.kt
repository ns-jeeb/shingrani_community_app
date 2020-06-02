package com.locked.shingranicommunity.di2

import android.app.Application
import com.locked.shingranicommunity.TestClass
import com.locked.shingranicommunity.common.AndroidResourceProvider
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import com.locked.shingranicommunity.repositories.UserRepository
import dagger.Module
import dagger.Provides

@Module
class AppModuleProvides {

    @Provides
    @AppScope
    fun provideTestClass(application: Application) : TestClass {
        return TestClass(application)
    }
}