package com.locked.shingranicommunity.members

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class MemberApiRequestt@Inject constructor() : MemberApiRequestListener {
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
}


