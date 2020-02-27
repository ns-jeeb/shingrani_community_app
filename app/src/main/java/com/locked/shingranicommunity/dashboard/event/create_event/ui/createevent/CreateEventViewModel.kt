package com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import javax.inject.Inject

class CreateEventViewModel@Inject constructor(val itemEventHandler: DashboardItemRequestListener, val context: Context) : ViewModel() {

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
//        itemEventHandler.createEvent(eventFields,"")
    }
}
