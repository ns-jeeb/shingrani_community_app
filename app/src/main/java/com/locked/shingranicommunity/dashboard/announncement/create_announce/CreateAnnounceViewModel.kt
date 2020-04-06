package com.locked.shingranicommunity.dashboard.announncement.create_announce

import android.widget.EditText
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
        var createdItem = MutableLiveData<Item>()
            fieldTitle?.let {
                fields?.add(it)
            }
            fieldDetails?.let {
                fields?.add(it)
            }

        var adminUsers = userManger.getAdminUser().value?.admins
            for (i in 0 until adminUsers?.size!!){
                if (userManger.getCurrentUser()?._id?.contentEquals(adminUsers[i]._id)!!) {
                    createdItem = requestResponse.createAnnouncement(fields!!)
                }
            }
        return createdItem
    }
    fun titleValidation(title: EditText): Boolean{
        return return if (title.text.toString().length >= 3) {
            true
        }else{
            title.error = "Title at lest should 3 or more characters"
            false
        }
    }
    fun detailsValidation(title: EditText):Boolean{
        return if (title.text.toString().length >= 10) {
            true
        }else{
            title.error = "Title at lest should 10 or more characters"
            false
        }
    }

}