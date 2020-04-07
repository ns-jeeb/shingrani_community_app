package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.models.Admin
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(val itemEventHandler: DashboardItemRequestListener,val userManager: UserManager, val context: Context): ViewModel() {

    fun load(): List<Item> {

        var items: List<Item> = ArrayList()
        itemEventHandler.fetchEvent()
        return items
    }

    fun onRefresh() {
        viewModelScope.launch {
            itemEventHandler.fetchEvent()
        }
    }

    fun itemDelete(itemId: String):MutableLiveData<String>? {
        var response = MutableLiveData<String>()
        response.value ="You don't have permission to delete event"
        return if (getAdminUser()!= null){
            itemEventHandler.deleteFields(itemId)
        }else{
            response
        }
    }

    fun itemsLoaded(): LiveData<ArrayList<Item>> {
        return itemEventHandler.fetchEvent()!!
    }

    fun getCurrentUser(): User {
        return userManager.getCurrentUser()!!
    }

    fun updateItem(item: Item,inviteFiled: String): String {
        var message: String = ""
        for (i in 0 until item.fields!!.size) {
            item.fields?.associateBy {

                if (item.fields?.get(i)?.name == inviteFiled) {
                    if (!item.fields!![i].value?.contains(getCurrentUser()._id)!!) {
                        item.fields!![i].value = "${getCurrentUser()._id},"
                    } else {
                        Log.d("invited", "you are already invited")
                        return "You are invited"
                    }
                    itemEventHandler.updateItem(item.fields, item._id)
                }
            }
        }
        return message
    }

    fun getAdminUser(): Admin?{
        return userManager.getAdminUser(userManager.getCurrentUser()?._id!!)
    }
}