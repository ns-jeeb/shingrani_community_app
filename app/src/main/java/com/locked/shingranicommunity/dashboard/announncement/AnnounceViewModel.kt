package com.locked.shingranicommunity.dashboard.announncement

import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.dashboard.I_FetchedEventAnnouncements
import com.locked.shingranicommunity.dashboard.data.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class AnnounceViewModel(val IFetchedEventAnnouncements: I_FetchedEventAnnouncements) : ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner
    val _fetchItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }

    fun load(): List<Item> {
        var items: List<Item> = ArrayList()
        IFetchedEventAnnouncements.cachedData?.observe(lifecycleOwner as AnnounceFragment, Observer {
            _fetchItems.postValue(it)

        })
        return items
    }

    fun onRefresh() {
        // Launch a coroutine that reads from a remote data source and updates cache
        viewModelScope.launch {
            IFetchedEventAnnouncements.fetchAnnouncement(TEMPLATE_ANNOUNCE)
        }
    }


    companion object {
        // Real apps would use a wrapper on the result type to handle this.
        const val TEMPLATE_ANNOUNCE = "5d70430b09c81c13001cbb88"
    }
}

object AnnounceItemVMFactory : ViewModelProvider.Factory {

    private val itemDataSource = DashboardRepositor(Dispatchers.IO)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AnnounceViewModel(itemDataSource) as T
    }
}

