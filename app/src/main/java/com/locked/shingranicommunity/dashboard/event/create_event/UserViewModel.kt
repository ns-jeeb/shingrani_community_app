package com.locked.shingranicommunity.dashboard.event.create_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.members.MemberApiRequest
import com.locked.shingranicommunity.members.ShingraniMember
import javax.inject.Inject

class UserViewModel @Inject constructor(var requestMembers: MemberApiRequest) : ViewModel()  {

    fun getMember(token: String): LiveData<ArrayList<ShingraniMember>> {
        return  requestMembers.members(token)
    }
}