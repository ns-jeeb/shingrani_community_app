package com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent

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
    fun createEvent(name:String,type:String,address: String,dateTime:String,accepted:String,rejected:String,note:String): MutableLiveData<String>{
        var nameField = Field(name = "name" ,value =  name )
        var typeField = Field(name = "type" ,value =  type )
        var addressField = Field(name = "address" ,value =  address )
        var dateTimeField = Field(name = "datetime" ,value =  dateTime )
        var acceptedField = Field(name = "Accepted" ,value =  accepted )
        var rejectedField = Field(name = "Rejected" ,value =  rejected )
        var noteField = Field(name = "note" ,value =  note )

     var eventFields: ArrayList<Field> = ArrayList();
        eventFields.add(nameField)
        eventFields.add(addressField)
        eventFields.add(dateTimeField)
        eventFields.add(typeField)
        eventFields.add(acceptedField)
        eventFields.add(rejectedField)
        eventFields.add(noteField)
        return itemEventHandler.createEvent(eventFields,Constant_Utils.EVENT_TIMPLATE_ID)!!
    }
}

//{
//    "_id": "5d770cd8eAa2f6b1300f03ca7",
//    "app": "5d4a348f88fb44130084f903",
//    "title": "Event",
//    "description": "Event is something that happens or takes place.",
//    "creator": "5d1134ab5609832210f63ef7",
//    "__v": 0,
//    "tags": [],
//    "fields": [
//    {
//        "name": "Name",
//        "type": "SingleLine",
//        "hidden": false,
//        "sensitive": false,
//        "important": true,
//        "required": true,
//        "value": null,
//        "example": null
//    },
//    {
//        "name": "Type",
//        "type": "SingleChoice",
//        "hidden": false,
//        "sensitive": false,
//        "important": false,
//        "required": true,
//        "value": "Event, Wedding, Party, Fundraising, Funeral, Gathering, Meeting, Outing",
//        "example": null
//    },
//    {
//        "name": "Address",
//        "type": "Address",
//        "hidden": false,
//        "sensitive": false,
//        "important": true,
//        "required": true,
//        "value": null,
//        "example": null
//    },
//    {
//        "name": "Time",
//        "type": "DateTime",
//        "hidden": false,
//        "sensitive": false,
//        "important": true,
//        "required": true,
//        "value": null,
//        "example": null
//    },
//    {
//        "name": "Detail",
//        "type": "MultiLine",
//        "hidden": false,
//        "sensitive": false,
//        "important": false,
//        "required": false,
//        "value": null,
//        "example": null
//    },
//    {
//        "name": "Accepted",
//        "type": "CSV",
//        "hidden": true,
//        "sensitive": false,
//        "important": false,
//        "required": false,
//        "value": null,
//        "example": null
//    },
//    {
//        "name": "Rejected",
//        "type": "CSV",
//        "hidden": true,
//        "sensitive": false,
//        "important": false,
//        "required": false,
//        "value": null,
//        "example": null
//    }
//    ],
//    "icon": {
//    "url": "https://cdn3.iconfinder.com/data/icons/cat-force/128/cat_cart.png"
//},
//    "colorDarker": "#8796A1",
//    "colorOriginal": "#B6C3CC"
//}
