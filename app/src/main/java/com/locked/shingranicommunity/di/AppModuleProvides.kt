package com.locked.shingranicommunity.di

import android.app.Application
import com.locked.shingranicommunity.TestClass
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