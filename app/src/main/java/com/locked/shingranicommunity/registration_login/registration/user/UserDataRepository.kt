
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
@LoggedUserScope
class UserDataRepository @Inject constructor (private val userManager: UserManager) {

    private var lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)
    val username: String
        get() = userManager.username

    var unreadNotifications: Int

    init {
        unreadNotifications = randomInt(lockedApiService)
    }

    fun refreshUnreadNotifications() {
        unreadNotifications = randomInt(lockedApiService)
    }


}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class AuthenticationRepository(var loginEvent: LoginEvent?, var registerEvent: OnAuthenticatedSuccess?) {

    var user: LoggedInUser? = null
    var result : Result<LoggedInUser>? = null
    val sharedPreferences: SharedPreferences = MyApplication.instance.getSharedPreferences("token", Context.MODE_PRIVATE)

    private var lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)

    fun setOnLoginEvent(loginEvent: LoginEvent?){
        this.loginEvent = loginEvent
    }
    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        sharedPreferences.edit().putString("token", "").commit()
    }

    fun login(username: String, password: String): Result<LoggedInUser>? {
        // handle login

        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap.put("username",username)
        bodyHashMap.put("password",password)

        val call = lockedApiService.userLogin(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
            override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {

                if (response.isSuccessful){
                    loginEvent?.onLoginSuccess(true)
                    var token = response.body()?.token
                    var user : User? = response.body()?.user

                    savedToken(token, sharedPreferences)
                    setLoggedInUser(response.body()!!)
                }else{
                    loginEvent?.onLoginFailed(response.message())
                }
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
            loginEvent?.onLoginSuccess(true)
            return result
        }
        return null
    }

    fun onRegisterEvent(registerEvent: OnAuthenticatedSuccess?){
        this.registerEvent = registerEvent
    }

    fun register(username: String, password: String,name: String): Result<LoggedInUser>? {

        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap["password"] = password
        bodyHashMap["username"] = username
        bodyHashMap["name"] = name

        val call = lockedApiService.registerUser(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<RegisterUser> {
            override fun onResponse(call: Call<RegisterUser>, response: Response<RegisterUser>) {
                if (response.isSuccessful){
                    var user = response.body()
                    var error : String = response.message()
                    registerEvent?.onSuccess()
                    Log.v("LoggedinUser", "${"token"}*********  " + "**${error} ** ${user?.name}")
                }else{
                    registerEvent?.onFailed()
                }

            }

            override fun onFailure(call: Call<RegisterUser>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                registerEvent?.onFailed()
            }
        })

        return result
    }
    interface OnAuthenticatedSuccess{
        fun onSuccess()
        fun onFailed()
    }

}


fun login(username: String, password: String, lockedApiService: LockedApiServiceInterface) {
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

fun randomInt(lockedApiService: LockedApiServiceInterface): Int {

    var bodyHashMap: HashMap<String, String> = HashMap()
    bodyHashMap.put("username","najeeb1@gmail.com")
    bodyHashMap.put("password","abc123")

    val call = lockedApiService.userLogin(bodyHashMap)
    call.enqueue(object : Callback, retrofit2.Callback<LoggedInUser> {
        override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {

            if (response.isSuccessful){
//                loginEvent?.onLoginSuccess(true)
                var token = response.body()?.token
                var user : User? = response.body()?.user

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
    return Random.nextInt(until = 100)
}
