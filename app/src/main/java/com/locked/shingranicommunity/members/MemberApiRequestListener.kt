package com.locked.shingranicommunity.members

import androidx.lifecycle.MutableLiveData
import java.lang.reflect.Member

interface MemberApiRequestListener {
    fun members(token: String): MutableLiveData<ArrayList<ShingraniMember>>
}