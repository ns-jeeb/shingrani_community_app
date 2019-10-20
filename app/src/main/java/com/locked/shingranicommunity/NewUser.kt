package com.locked.shingranicommunity

import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

class NewUser {
    var name = ObservableField<String>()
    var lastName = ObservableField<String>()
    var age = ObservableInt()
    var arrayList = ObservableArrayMap<String, Any>().apply{
        put("firstName", "Najeeb")
        put("lastName","Sakhizada")
        put("age",41)

    }

}
