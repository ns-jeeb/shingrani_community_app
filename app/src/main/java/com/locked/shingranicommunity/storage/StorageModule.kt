package com.locked.shingranicommunity.storage

import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.members.MemberApiRequestListener
import com.locked.shingranicommunity.members.MemberApiRequest
import com.locked.shingranicommunity.registration_login.registration.user.UserDataRepository
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
    @Binds
    abstract fun bindReposRespons(reopos: DashboardRepositor): DashboardItemRequestListener

    @Binds
    abstract fun proideCallBackEvent(userDataRepository: UserDataRepository): ResponseEvent
    @Binds
    abstract fun memberModuleProvider(memberApiRequest: MemberApiRequest): MemberApiRequestListener
}