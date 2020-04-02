package com.locked.shingranicommunity.members

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
                if (response.isSuccessful){
                    mtableLiveData.value = response.body()
                }else{
                    mtableLiveData.value?.get(0)?.message = parsingJson(response.errorBody())
                }

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
                    message.value = parsingJson(response.errorBody())
                }
            }
        })
        return message
    }

    fun parsingJsonArray(array: JSONArray, key: String): String {

        var errorMessage = ""
        if (array.length() != 0 && key.isNotEmpty()) {
            for (i in 0 until array.length()) {
                var arraylist = array.getJSONObject(i)
                errorMessage = arraylist.getString("message")
            }
        }
        return errorMessage
    }
    fun parsingJson(responsebody: ResponseBody?): String {
        var message =""
        try {
            val errorBody = responsebody!!.string()
            var jsonObject = JSONObject(errorBody.trim { it <= ' ' })
//            var message = jsonObject.getString("message")
            var errorJson = jsonObject.getJSONArray("errors")
            message = parsingJsonArray(errorJson, "message")
        } catch (e: JSONException) {
            e.printStackTrace().toString()
        }
        return message
    }
}


