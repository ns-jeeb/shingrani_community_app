package com.locked.shingranicommunity.dashboard.announncement.create_announce

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class CreateAnnounceViewModel@Inject constructor(val userManger: UserManager,val requestResponse: DashboardRepositor): ViewModel() {
    fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
    fun createAnnouncement(title: String, details: String): LiveData<String>{
        var fields: ArrayList<Field>? = ArrayList()
        var fieldTitle: Field? = Field(name = "name", value = title)
        var fieldDetails: Field? = Field(name = "name", value = details)
        var createdItem = MutableLiveData<String>()
        createdItem.value = "You don't have permission to create announcement"
            fieldTitle?.let {
                fields?.add(it)
            }
            fieldDetails?.let {
                fields?.add(it)
            }

        var adminUsers = userManger.getAppModel()?.admins
            for (i in 0 until adminUsers?.size!!){
                return if (userManger.getCurrentUser()?._id?.contentEquals(adminUsers[i]._id)!!) {
                    requestResponse.createAnnouncement(fields!!)
                }else{
                    createdItem
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