package com.locked.shingranicommunity.members

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class MemberApiRequest @Inject constructor(val storage: Storage) : MemberApiRequestListener {
    var lockedApiServiceInterface = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)
    var mtableLiveData = MutableLiveData<ArrayList<ShingraniMember>>()
    override fun members(token: String): MutableLiveData<ArrayList<ShingraniMember>>{

        var call = lockedApiServiceInterface.getMembers(token)

        call.enqueue(object : Callback, retrofit2.Callback<ArrayList<ShingraniMember>>{
            override fun onFailure(call: Call<ArrayList<ShingraniMember>>, t: Throwable) {
                Log.d("OnFailureApiCall","didn't call successfully")
            }

            override fun onResponse(call: Call<ArrayList<ShingraniMember>>, response: Response<ArrayList<ShingraniMember>>) {
                mtableLiveData.value = response.body()
                storage.setUser(mtableLiveData.value!!)
            }

        })
        return mtableLiveData
    }

    override fun inviteMember(email: String, name: String, token: String): MutableLiveData<String> {
        var message = MutableLiveData<String>()
        var inviteInfo = HashMap<String, String>()
        var domain = ""
        inviteInfo["email"] = email
        domain = if (name.isEmpty()) {
            var indexOfAt = email.split('@')
            indexOfAt[0]
        }else{
            name
        }
        inviteInfo["name"] = domain
        var call = lockedApiServiceInterface.inviteMember(inviteInfo,token)
        call.enqueue(object : Callback, retrofit2.Callback<ShingraniMember>{
            override fun onFailure(call: Call<ShingraniMember>, t: Throwable) {
                Log.d("InviteMember","onFailure $t")
            }
            override fun onResponse(call: Call<ShingraniMember>, response: Response<ShingraniMember>) {
                if (response.isSuccessful) {
                     message.value = response.body()?.message
                    Log.d("InviteMember","onResponse ${response.body()}")
                }else{
                    message.value = response.body()?.user?.name
                }
            }
        })
        return message
    }
}


