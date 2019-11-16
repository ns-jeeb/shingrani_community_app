package com.locked.shingranicommunity.dashboard

import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.data.Item

class DashboardInterfaces{

}
interface I_FetchedEventAnnouncements {
    fun getFields(): LiveData<List<Item>>?
    fun fetchEvent(template: String): LiveData<List<Item>>?
    fun fetchAnnouncement(template: String): LiveData<List<Item>>?
    val cachedData: LiveData<List<Item>>?
    suspend fun fetchNewItem()
}
