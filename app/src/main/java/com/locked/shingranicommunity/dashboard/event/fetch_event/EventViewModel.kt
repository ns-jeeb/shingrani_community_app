package com.locked.shingranicommunity.dashboard.event.fetch_event

import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.dashboard.DataSource
import com.locked.shingranicommunity.dashboard.data.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class EventViewModel(val dataSource: DataSource) : ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner

    val _fetchItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }

    fun load(): List<Item> {
        var items: List<Item> = ArrayList()
        dataSource.cachedData?.observe(lifecycleOwner as EventListFragment, Observer {
            _fetchItems.postValue(it)

        })
        return items
    }
    fun onRefresh() {
        // Launch a coroutine that reads from a remote data source and updates cache
        viewModelScope.launch {
            dataSource.fetchEvent(TEMPLATE_EVENT)
        }
    }

    companion object {
        // Real apps would use a wrapper on the result type to handle this.
        const val TEMPLATE_EVENT = "5d770cd8ea2f6b1300f03ca7"
    }

    object ItemLiveDataVMFactory : ViewModelProvider.Factory {

        private val itemDataSource = DashboardRepositor(Dispatchers.IO)

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(itemDataSource) as T
        }
    }
}
