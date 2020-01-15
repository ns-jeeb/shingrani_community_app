package com.locked.shingranicommunity.registration_login.registration.user

import android.util.Log
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.tutorials.RegisterUser
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback


class RegisterRepository @Inject constructor (private val userManager: UserManager) {

    private var lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)
    val username: String
        get() = userManager.username
    init {
    }

    fun refreshUnreadNotifications() {
    }

    fun register(username: String, password: String,name: String): String {
        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap["username"] = username
        bodyHashMap["password"] = password
        bodyHashMap["name"] = name
        var message = ""

        val call = lockedApiService.registerUser(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<RegisterUser> {
            override fun onResponse(call: Call<RegisterUser>, response: Response<RegisterUser>) {
                if (response.isSuccessful){
                    message = response.message()
                    userManager.userCreated(message)
                    Log.v("LoggedinUser", " ********* **${message} ")
                }else{

                }
            }

            override fun onFailure(call: Call<RegisterUser>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }
        })

        return message
    }

}