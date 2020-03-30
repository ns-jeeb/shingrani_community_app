package com.locked.shingranicommunity.dashboard.announncement.create_announce

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class CreateAnnounceViewModel@Inject constructor(val userManger: UserManager,val requestResponse: DashboardRepositor): ViewModel(){
    fun createAnnouncement(title: String, details: String): LiveData<Item>{
        var fields: ArrayList<Field>? = ArrayList()
        var fieldTitle: Field? = Field(name = "name", value = title)
        var fieldDetails: Field? = Field(name = "name", value = details)
        var strin = MutableLiveData<Item>()
            fieldTitle?.let {
                fields?.add(it)
            }
            fieldDetails?.let {
                fields?.add(it)
            }

        var adminId = userManger.getAdminUser().value?.admins?.get(0)?._id
            if (userManger.getCurrentUser()?._id?.contentEquals(adminId!!)!!) {

            }
        strin = requestResponse.createAnnouncement(fields!!)
        return strin
    }
}