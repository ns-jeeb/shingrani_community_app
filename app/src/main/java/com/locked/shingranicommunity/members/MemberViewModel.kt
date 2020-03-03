package com.locked.shingranicommunity.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MemberViewModel@Inject constructor(var requestMembers: MemberApiRequestt) : ViewModel() {

    fun getMember(token: String): LiveData<ArrayList<ShingraniMember>>{
        return  requestMembers.members(token)
    }

}