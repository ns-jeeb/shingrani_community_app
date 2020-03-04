package com.locked.shingranicommunity.members

import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import java.lang.reflect.Member

interface MemberApiRequestListener {
    fun members(token: String): MutableLiveData<ArrayList<ShingraniMember>>
    fun inviteMember(email: String, @Nullable name: String, token: String): MutableLiveData<String>
}