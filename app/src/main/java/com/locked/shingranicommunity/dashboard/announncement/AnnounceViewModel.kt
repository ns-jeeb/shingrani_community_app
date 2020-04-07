package com.locked.shingranicommunity.dashboard.announncement

import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.models.Admin
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class AnnounceViewModel @Inject constructor(val eventListener: DashboardItemRequestListener,val userManager: UserManager) : ViewModel() {

    val _fetchItems: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }

    fun load(): List<Item> {
        var items: List<Item> = ArrayList()
        eventListener.fetchAnnouncement()
        return items
    }

    fun onRefresh() {
        viewModelScope.launch {
            eventListener.fetchAnnouncement()
        }
    }

    fun loadedAnnouncements(): LiveData<ArrayList<Item>>{
        return eventListener.fetchAnnouncement()!!
    }
    fun getAdminUser(): Admin?{
        return userManager.getAdminUser(userManager.getCurrentUser()?._id!!)
    }

}


