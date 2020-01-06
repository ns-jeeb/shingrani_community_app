package com.locked.shingranicommunity.dashboard

import com.locked.shingranicommunity.di.DashboardComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashBoardManager @Inject constructor (private val dashBoardManager: DashboardComponent.Factory) {
}