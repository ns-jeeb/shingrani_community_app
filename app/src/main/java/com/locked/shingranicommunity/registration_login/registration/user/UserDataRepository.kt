
package com.locked.shingranicommunity.registration_login.registration.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.data.Result
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import com.locked.shingranicommunity.di.scops.ActivityScope
import com.locked.shingranicommunity.di.scops.LoggedUserScope
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.tutorials.RegisterUser
import com.locked.shingranicommunity.tutorials.User
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback
import kotlin.random.Random

/**
 * UserDataRepository contains user-specific data such as username and unread notifications.
 */

class UserDataRepository @Inject constructor (private val userManager: UserManager) {

    private var lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)
    val username: String
        get() = userManager.username

//    var unreadNotifications: Int

//    init {
//        unreadNotifications = randomInt(lockedApiService)
//    }
//
//    fun refreshUnreadNotifications() {
//        unreadNotifications = randomInt(lockedApiService)
//    }

    fun login(username: String, password: String) {
        // handle login

        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap.put("username",username)
        bodyHashMap.put("password",password)

        val call = lockedApiService.userLogin(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
            override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {

                if (response.isSuccessful){
//                loginEvent?.onLoginSuccess(true)
                    var token = response.body()?.token
                    var user : User? = response.body()?.user
                    userManager.saveUser(username, token!!)

//                savedToken(token, sharedPreferences)
//                setLoggedInUser(response.body()!!)
                }else{
//                loginEvent?.onLoginFailed(response.message())
                }
            }

            override fun onFailure(call: Call<LoggedInUser>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }
        })




    }


}
