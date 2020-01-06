package com.locked.shingranicommunity.dashboard

import com.locked.shingranicommunity.Constant_Utils.EVENT_TIMPLATE_ID
import javax.inject.Inject


class DashBoardViewModel @Inject constructor(private val dashboardRepositor: DashboardRepositor) {
    fun loadItem(){
        dashboardRepositor.loadEvent(EVENT_TIMPLATE_ID)
    }
}