package com.locked.shingranicommunity.storage

import com.locked.shingranicommunity.di.Storage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}