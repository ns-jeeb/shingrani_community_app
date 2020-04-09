package com.locked.shingranicommunity.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

class MemberViewModel@Inject constructor(var userManager: UserManager,var requestMembers: MemberApiRequest) : ViewModel() {

    fun getMember(): LiveData<ArrayList<ShingraniMember>>{
        var arrayMembers = MutableLiveData <ArrayList<ShingraniMember>>()
        arrayMembers = requestMembers.members(userManager.token)
        if (UserManager.memeberUser.value!= null && UserManager.memeberUser.value?.size != 0) {
            arrayMembers = UserManager.memeberUser
        }
        return  arrayMembers
    }
}