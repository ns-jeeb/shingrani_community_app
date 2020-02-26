package com.locked.shingranicommunity.dashboard

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.Constant_Utils.EVENT_TIMPLATE_ID
import javax.inject.Inject


class DashBoardViewModel @Inject constructor(private val dashboardRepositor: DashboardRepositor):ViewModel() {
    fun loadItem(){
        dashboardRepositor.loadEvent(EVENT_TIMPLATE_ID)
    }
}