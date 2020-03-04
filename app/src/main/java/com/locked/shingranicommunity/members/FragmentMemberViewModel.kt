package com.locked.shingranicommunity.members

import androidx.annotation.Nullable
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.di.Storage
import javax.inject.Inject

class FragmentMemberViewModel @Inject constructor(var requestMemberApi: MemberApiRequestListener,private val storage: Storage) : ViewModel() {
    fun inviteMember(email: String, @Nullable name: String){
        requestMemberApi.inviteMember(email, name ,storage.getToken("token"))
    }
}
