package com.locked.shingranicommunity.dashboard.event.create_event

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.dashboard.DashboardItemRequestListener
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import javax.inject.Inject

class CreateEventViewModel@Inject constructor(val itemEventHandler: DashboardItemRequestListener, val context: Context) : ViewModel() {

    private val createEvent : MutableLiveData<Item> by lazy {
        MutableLiveData<Item>()
    }
    fun createEvent(name:String,type:String,address: String,dateTime:String,moreDetails:String,accepted:String,rejected:String,note:String): MutableLiveData<String>{
        var nameField = Field(name = "name" ,value =  name )
        var typeField = Field(name = "type" ,value =  type )
        var addressField = Field(name = "address" ,value =  address )
        var dateTimeField = Field(name = "datetime" ,value =  dateTime )
        var moreDetails = Field(name = "moreDetails" ,value = moreDetails)
        var acceptedField = Field(name = "Accepted" ,value =  accepted )
        var rejectedField = Field(name = "Rejected" ,value =  rejected )
        var noteField = Field(name = "note" ,value =  note )

     var eventFields: ArrayList<Field> = ArrayList();
        eventFields.add(nameField)
        eventFields.add(addressField)
        eventFields.add(dateTimeField)
        eventFields.add(typeField)
        eventFields.add(moreDetails)
        eventFields.add(acceptedField)
        eventFields.add(rejectedField)
        eventFields.add(noteField)
        return itemEventHandler.createEvent(eventFields,Constant_Utils.EVENT_TIMPLATE_ID)!!
    }
}
