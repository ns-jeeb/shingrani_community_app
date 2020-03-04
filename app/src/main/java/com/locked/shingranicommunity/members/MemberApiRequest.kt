package com.locked.shingranicommunity.members

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class MemberApiRequest @Inject constructor() : MemberApiRequestListener {
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
            }

        })
        return mtableLiveData
    }

    override fun inviteMember(email: String, @Nullable name: String, token: String): MutableLiveData<String> {
        var message = MutableLiveData<String>()
        var inviteInfo = HashMap<String, String>()
        inviteInfo.put("email",email)
        inviteInfo.put("name",name)
        var call = lockedApiServiceInterface.inviteMember(inviteInfo,token)

        call.enqueue(object : Callback, retrofit2.Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful) {
                     message.value = response.body()
                }else{
                    message.value = "there is something wrong"
                }
            }

        })
        return message
    }
}


