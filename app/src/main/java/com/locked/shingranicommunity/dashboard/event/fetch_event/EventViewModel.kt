package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.content.Context
import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class EventViewModel @Inject constructor(val userManager: UserManager, val context: Context): ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner

    val _fetchItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }

    fun load(): List<Item> {

        var items: List<Item> = ArrayList()
//        IItemEventListener.cachedData?.observe(lifecycleOwner as EventListFragment, Observer {
//            _fetchItems.postValue(it)
//
//        })
        return items
    }
    fun onRefresh() {
        // Launch a coroutine that reads from a remote data source and updates cache
        viewModelScope.launch {
//            IItemEventListener.fetchEvent(TEMPLATE_EVENT)
        }
    }
    fun itemDelete(itemId: String,token:String){
//        IItemEventListener.deleteFields(itemId,token)
    }

    companion object {
        // Real apps would use a wrapper on the result type to handle this.
        const val TEMPLATE_EVENT = "5d770cd8ea2f6b1300f03ca7"
    }

//    object EventItemVMFactory : ViewModelProvider.Factory {
//
//        private val itemDataSource = DashboardRepositor()
//
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            @Suppress("UNCHECKED_CAST")
//            return EventViewModel(itemDataSource) as T
//        }
//    }
}
