package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.common.AndroidResourceProvider
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.session.Session
import com.locked.shingranicommunity.session.SessionManager
import dagger.Binds
import dagger.Module

@Module(includes = [AppModuleProvides::class])
interface AppModule {

    @Binds
    @AppScope
    fun provideResourceProvider(resourceProvider: AndroidResourceProvider) : ResourceProvider

    @Binds
    @AppScope
    fun provideSession(session: SessionManager) : Session
}