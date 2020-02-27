package com.locked.shingranicommunity.dashboard

import com.locked.shingranicommunity.di.DashboardComponent
import dagger.Binds
import dagger.Module

@Module
abstract class DashboardProviderModule {
    @Binds
    abstract fun bindDashboardReqProvider(dashRequestListener: DashboardRepositor):IItemEventListener

//    @Binds
//    abstract fun bindDashboardResProvider(dashResponseListener: DashboardResponse):IDashboardResponseLister
}