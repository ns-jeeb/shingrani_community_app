package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.data.Rsvp
import com.locked.shingranicommunity.dashboard.data.RsvpObject
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.models.Admin
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class EventViewModel @Inject constructor(private val itemEventHandler: DashboardItemRequestListener, val userManager: UserManager, val context: Context): ViewModel() {

    fun itemDelete(itemId: String):MutableLiveData<String>? {
        var response = MutableLiveData<String>()
        response.value ="You don't have permission to delete event"
        return if (getAdminUser()!= null){
            itemEventHandler.deleteFields(itemId)
        }else{
            response
        }
    }
    fun memberAttend(items: ArrayList<Item>):ArrayList<String>{
        var accepts = ArrayList<String>()
        for (i in 0 until items.size){
            when(items[i].fields?.get(5)?.name){
                "Accepted"->{
                    for (element in items[i].fields?.get(5)?.value?.split(",")!!){
                        accepts.add(element)
                    }
                }
            }
        }
        return accepts
    }
    fun itemsLoaded(): LiveData<ArrayList<Item>> {
        return itemEventHandler.fetchEvent()!!
    }

    fun getCurrentUser(): User {
        return userManager.getCurrentUser()!!
    }

    fun accepted(item: Item): MutableLiveData<String>? {
        val rsvp: RsvpObject? = RsvpObject(Rsvp("Accepted", userManager.getCurrentUser()?._id!!))
        return itemEventHandler.accepted(rsvp!!,item._id)
    }

    fun rejected(item: Item): MutableLiveData<String>? {
        var rsvp: RsvpObject? = RsvpObject(Rsvp("Rejected", userManager.getCurrentUser()?._id!!))
        return itemEventHandler.rejected(rsvp!!,item._id)
    }

    fun updateItem(item: Item,inviteFiled: String): MutableLiveData<String>? {
        var message: MutableLiveData<String> = MutableLiveData()
        for (i in 0 until item.fields!!.size) {
            item.fields?.associateBy {

                if (item.fields?.get(i)?.name == inviteFiled) {
                    if (!item.fields!![i].value?.contains(getCurrentUser()._id)!!) {
                        item.fields!![i].value = "${getCurrentUser()._id},"
                    } else {
                        Log.d("invited", "you are already invited")
                        message.value =  "You are invited"
                    }
                    return itemEventHandler.updateItem(item.fields, item._id)
                }
            }
        }
        return message
    }

    fun getAdminUser(): Admin?{
        return userManager.getAdminUser(userManager.getCurrentUser()?._id!!)
    }
}