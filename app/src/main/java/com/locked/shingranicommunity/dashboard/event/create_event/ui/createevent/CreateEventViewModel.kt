package com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import kotlinx.coroutines.Dispatchers

class CreateEventViewModel(var createDataSource: DashboardRepositor) : ViewModel() {
    lateinit var lifecycleOwner: LifecycleOwner
    private val createEvent : MutableLiveData<Item> by lazy {
        MutableLiveData<Item>()
    }
    fun createEvent(name:String,type:String,address: String,dateTime:String,note:String){
        var nameField = Field(name = "name" ,value =  name )
        var typeField = Field(name = "type" ,value =  type )
        var addressField = Field(name = "address" ,value =  address )
        var dateTimeField = Field(name = "datetime" ,value =  dateTime )
        var noteField = Field(name = "note" ,value =  note )

     var eventFields: ArrayList<Field> = ArrayList();
        eventFields.add(nameField)
        eventFields.add(addressField)
        eventFields.add(dateTimeField)
        eventFields.add(typeField)
        eventFields.add(noteField)
        createDataSource.createEvent(eventFields,"")
    }
}



object CreateEventItemVMFactory : ViewModelProvider.Factory {

    private val itemDataSource = DashboardRepositor(Dispatchers.IO)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CreateEventViewModel(itemDataSource) as T
    }
}