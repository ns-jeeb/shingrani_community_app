package com.locked.shingranicommunity.di.locked

import com.locked.shingranicommunity.di.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedApiServiceBuilder
import com.locked.shingranicommunity.session.Session
import dagger.Module
import dagger.Provides

@Module
class LockedApiServiceModule {

    @AppScope
    @Provides
    fun provideLockedApiService(session: Session): LockedApiService {
        return LockedApiServiceBuilder(session).build()
    }
}