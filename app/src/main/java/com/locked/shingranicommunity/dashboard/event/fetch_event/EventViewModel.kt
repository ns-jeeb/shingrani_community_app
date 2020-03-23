package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(val itemEventHandler: DashboardItemRequestListener, private val userManager: UserManager): ViewModel() {

    fun updateItem(item: Item): String {
        var message: String = ""
        var field : ArrayList<Field>? = item.fields

        for (name in item?.fields!!) {
            var newField = item.fields?.associateBy {
                if (it.name !== "Accepted")
                    3

                if (!name.name?.contains("Accepted")!!) {
                    name.name = "Accepted"
                    name.value = getCurrentUser()?._id
                } else {
                    if(!name.value?.contains(getCurrentUser()?._id!!)!!){
                        name.value = getCurrentUser()?._id
                    }else{
                        Log.d("invited", "you are already invited")
                        return "You are invited"
                    }
                }
                field?.add(name)
                itemEventHandler.updateItem(field, item._id)
            }
        }
        return message
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
    fun getCurrentUser() : User?{
        return userManager.getCurrentUser()
    }

    fun getUsers(): MutableLiveData<ArrayList<ShingraniMember>> {
        return userManager.getUsers()
    }

}
