package com.locked.shingranicommunity.tutorials

import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class UserRepository {
    private val webservice: LockedApiServiceInterface = TODO()
    // ...
    fun getUser(userId: String): MutableLiveData<LoggedInUser> {
        var bodyHash : HashMap<String, String> = HashMap()
        var username: String = "ns.jeeb@gmail.com"
        var password : String = "abc123"
        bodyHash.put("username",username)
        bodyHash.put("password",password)
        // This isn't an optimal implementation. We'll fix it later.
        val data = MutableLiveData<LoggedInUser>()
        webservice.userLogin(bodyHash).enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
            override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {

            }

            override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {
                data.value = response.body()
            }

        })
        return data
    }
}

