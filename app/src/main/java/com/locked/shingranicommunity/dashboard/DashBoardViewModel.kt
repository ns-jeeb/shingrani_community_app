package com.locked.shingranicommunity.dashboard

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import javax.inject.Inject


class DashBoardViewModel @Inject constructor(private val dashboardRepositor: DashboardRepositor):ViewModel() {
    fun loadItem(context: Context){
        Toast.makeText(context,"this is working",Toast.LENGTH_LONG).show()
    }
}