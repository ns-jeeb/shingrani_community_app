
package com.locked.shingranicommunity.registration_login.registration.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.registration_login.registration.login.LoginSuccess
import com.locked.shingranicommunity.registration_login.registration.login.LoginViewState
//import com.locked.shingranicommunity.authenticate.LoginEvent
//import com.locked.shingranicommunity.authenticate.data.Result
import com.locked.shingranicommunity.storage.model.LoggedInUser
//import com.locked.shingranicommunity.tutorials.User
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

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

    fun login(username: String, password: String) : LiveData<LoginViewState> {
        // handle login

        var loginSuccess = MutableLiveData<LoginViewState>()

        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap.put("username",username)
        bodyHashMap.put("password",password)

        val call = lockedApiService.userLogin(bodyHashMap,"5d4a348f88fb44130084f903")
        call.enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
            override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {

                var message = response.message()
                if (response.isSuccessful){
                    var token = response.body()?.token
                    userManager.saveUser(username, token!!)
                    loginSuccess.value = LoginSuccess

                }else{
                }
            }

            override fun onFailure(call: Call<LoggedInUser>?, t: Throwable?) {
                Log.v("retrofit", "call failed ${t?.message}")
            }
        })
        return loginSuccess
    }


}
