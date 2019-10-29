package com.locked.shingranicommunity.authenticate.data

import android.util.Log
import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import com.locked.shingranicommunity.authenticate.ui.login.LoginViewModel
import com.locked.shingranicommunity.tutorials.User
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.collections.HashMap

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    private lateinit var lockedApiService: LockedApiServiceInterface
    var user: LoggedInUser? = null
    var logingEvent = LoginViewModel(this )

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)!!
        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap.put("username",username)
        bodyHashMap.put("password",password)
        var result : Result<LoggedInUser>? = dataSource.login(User("", "", "",false,false),"")
        val call = lockedApiService.userLogin(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
            override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {

                var token = response.body()?.token
                var user = response.body()?.user


               result = user?.let { token?.let {username?.let{it1 -> dataSource.login(user,token) }} }
//                setLoggedInUser((result as Result.Success<LoggedInUser>).data as LoggedInUser)
                Log.v("LoggedinUser", "${token}*********  "+ "**${user?.username} ** ${user?.name}")
            }

            override fun onFailure(call: Call<LoggedInUser>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }
        })

        return result!!
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

}

