package com.locked.shingranicommunity.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class MemberViewModel@Inject constructor(var userManager: UserManager,var requestMembers: MemberApiRequest) : ViewModel() {

    fun getMember(token: String): LiveData<ArrayList<ShingraniMember>>{
        var arrayMembers = MutableLiveData <ArrayList<ShingraniMember>>()
        arrayMembers = requestMembers.members(token)
        if (userManager.getUsers().value?.size != null && userManager.getUsers().value?.size != 0) {
            arrayMembers = userManager.getUsers()
        }
        return  arrayMembers
    }
//    fun inviteMember(token: String) : LiveData<String>{
//        return requestMembers.inviteMember(token)
//    }


}