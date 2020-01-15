package com.locked.shingranicommunity.dashboard

import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.di.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashBoardManager @Inject constructor (private val repositor: Repository, private val dashBoardManager: DashboardComponent.Factory) {
    fun loadItem(){
        repositor.loadingItem()
    }
}