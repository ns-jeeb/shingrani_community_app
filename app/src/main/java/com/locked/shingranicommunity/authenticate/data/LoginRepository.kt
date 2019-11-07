package com.locked.shingranicommunity.authenticate.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import com.locked.shingranicommunity.authenticate.ui.login.LoggedInUserView
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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    private lateinit var lockedApiService: LockedApiServiceInterface
    var user: LoggedInUser? = null
    var logingEvent = LoginViewModel(this )
    var result : Result<LoggedInUser>? = null

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser>? {
        // handle login
        val sharedPreferences = CommunityApp.instance.getSharedPreferences("token",Context.MODE_PRIVATE)
        lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)!!
        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap.put("username",username)
        bodyHashMap.put("password",password)

        val call = lockedApiService.userLogin(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
            override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {

                var token = response.body()?.token
                var user = response.body()?.user
                savedToken(token, sharedPreferences)
                setLoggedInUser(response.body()!!)
                result = user?.let { token?.let { username?.let { it1 -> dataSource.login(user, token) } } }!!
                Log.v("LoggedinUser", "${token}*********  " + "**${user?.username} ** ${user?.name}")
            }

            override fun onFailure(call: Call<LoggedInUser>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }
        })

        return result


    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

    @SuppressLint

        ("CommitPrefEdits")
    fun savedToken(token: String?, sharedPreferences: SharedPreferences){

        var saveSharePerf = sharedPreferences.edit()
        saveSharePerf?.putString("token",token)
        saveSharePerf?.apply()

    }

    @Suppress("NAME_SHADOWING")
    fun getToken(sharedPreferences: SharedPreferences) : Result<LoggedInUser>? {

        var stringToken = sharedPreferences.getString("token","") !== null && sharedPreferences.getString("token","")!==""
        if (stringToken){
            result =  dataSource.logingWithToken(user,sharedPreferences.getString("token",""))
            return result
        }
        return null
    }

}

