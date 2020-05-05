package com.locked.shingranicommunity.dashboard
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.models.Field
import com.locked.shingranicommunity.models.Item
import com.locked.shingranicommunity.locked.models.RsvpObject

interface DashboardItemRequestListener {

    fun deleteFields(itemId:String): MutableLiveData<String>?
    fun fetchEvent(): MutableLiveData<ArrayList<Item>>?
    fun updateItem(fields: ArrayList<Field>?, itemId: String?): MutableLiveData<String>?
    fun accepted(rsvp: RsvpObject, itemId: String?): MutableLiveData<String>?
    fun rejected(rsvp: RsvpObject, itemId: String?): MutableLiveData<String>?
    fun fetchAnnouncement(): MutableLiveData<ArrayList<Item>>?
    fun createAnnouncement(fields: ArrayList<Field>): MutableLiveData<String>
    fun createEvent(fields: ArrayList<Field>): MutableLiveData<String>?
    suspend fun fetchNewItem()
}