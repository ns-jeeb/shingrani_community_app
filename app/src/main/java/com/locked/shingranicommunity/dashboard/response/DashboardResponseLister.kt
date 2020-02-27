package com.locked.shingranicommunity.dashboard.response

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.dashboard.data.Item

interface DashboardResponseLister {
    fun onLoadItems(items: ArrayList<LiveData<Item>>)
    fun onLoadEvents(items:LiveData<Item>)
    fun onLoadAnnouncements(items:MutableLiveData<ArrayList<Item>>)
}