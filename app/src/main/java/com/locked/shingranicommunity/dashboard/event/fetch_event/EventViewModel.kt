package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.content.Context
import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Item
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(val itemEventHandler: DashboardItemRequestListener, val context: Context): ViewModel() {

    fun load(): List<Item> {

        var items: List<Item> = ArrayList()
        itemEventHandler.fetchEvent(TEMPLATE_EVENT)
        return items
    }
    fun onRefresh() {
        viewModelScope.launch {
            itemEventHandler.fetchEvent(TEMPLATE_EVENT)
        }
    }
    fun itemDelete(itemId: String,token:String){
        itemEventHandler.deleteFields(itemId,token)
    }

    companion object {
        const val TEMPLATE_EVENT = "5d770cd8ea2f6b1300f03ca7"
    }

    fun itemsLoaded():LiveData<ArrayList<Item>>{
        return itemEventHandler.fetchEvent(TEMPLATE_EVENT)!!
    }

}
