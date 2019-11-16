package com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import kotlinx.coroutines.Dispatchers

class CreateEventViewModel(createDataSource: DashboardRepositor) : ViewModel() {
    lateinit var lifecycleOwner: LifecycleOwner
//    private val createEvent: MutableLiveData<Item>(){
//        MutableLiveData<Item>()
//    }
//}
//
//class CreateEventViewModelProvider : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        private val itemDataSource = DashboardRepositor(Dispatchers.IO)
//        return
//    }

}

object CreateEventItemVMFactory : ViewModelProvider.Factory {

    private val itemDataSource = DashboardRepositor(Dispatchers.IO)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CreateEventViewModel(itemDataSource) as T
    }
}