package com.locked.shingranicommunity.dashboard.announncement

import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Item
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class AnnounceViewModel @Inject constructor(val eventListener: DashboardItemRequestListener) : ViewModel() {

    val _fetchItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }

    fun load(): List<Item> {
        var items: List<Item> = ArrayList()
        eventListener.fetchAnnouncement(TEMPLATE_ANNOUNCE)
        return items
    }

    fun onRefresh() {
        viewModelScope.launch {
            eventListener.fetchAnnouncement(TEMPLATE_ANNOUNCE)
        }
    }

    fun loadedAnnouncements(): LiveData<ArrayList<Item>>{
        return eventListener.fetchAnnouncement(TEMPLATE_ANNOUNCE)!!
    }

    companion object {
        const val TEMPLATE_ANNOUNCE = "5d70430b09c81c13001cbb88"
    }
}


