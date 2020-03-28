package com.locked.shingranicommunity.dashboard.announncement.create_announce

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.dashboard.DashboardRepositor
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class CreateAnnounceViewModel@Inject constructor(val userManger: UserManager,val requestResponse: DashboardRepositor): ViewModel(){
    fun createAnnouncement(): String{
        if (userManger.getCurrentUser()?._id == userManger.getAdminUser().value?._id) {
            requestResponse.createAnnouncement("")
        }
        return ""
    }
}