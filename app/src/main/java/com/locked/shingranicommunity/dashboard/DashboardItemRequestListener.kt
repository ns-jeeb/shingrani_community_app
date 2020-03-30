package com.locked.shingranicommunity.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item

interface DashboardItemRequestListener {

    fun deleteFields(itemId:String,token: String): String?
    fun getFields(): LiveData<ArrayList<Item>>?
    fun fetchEvent(template: String): MutableLiveData<ArrayList<Item>>?
    fun updateItem(fields: ArrayList<Field>?,itemId: String?): String
    fun fetchAnnouncement(template: String): MutableLiveData<ArrayList<Item>>?
    fun createAnnouncement(fields: ArrayList<Field>):MutableLiveData<String>
    fun createEvent(fields: ArrayList<Field>): MutableLiveData<String>?
    val cachedData: LiveData<ArrayList<Item>>?
    suspend fun fetchNewItem()
}