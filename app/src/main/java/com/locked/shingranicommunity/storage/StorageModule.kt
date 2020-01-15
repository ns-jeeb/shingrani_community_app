package com.locked.shingranicommunity.storage

import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.mesages.DisplayResponseEvent
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
    @Binds
    abstract fun proideCallBackEvent(displayResponse: DisplayResponseEvent): ResponseEvent
}