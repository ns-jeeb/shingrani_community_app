
package com.locked.shingranicommunity.registration_login.registration.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.registration_login.registration.login.LoginFormState
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class UserDataRepository @Inject constructor (private val userManager: UserManager) {

    private var lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)
    val username: String
        get() = userManager.username

    fun login(username: String, password: String) : LiveData<LoginFormState> {
        var loginSuccess = MutableLiveData<LoginFormState>()

        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap.put("username",username)
        bodyHashMap.put("password",password)

        val call = lockedApiService.userLogin(bodyHashMap,"5d4a348f88fb44130084f903")
        call.enqueue(object : Callback, retrofit2.Callback<ShingraniMember> {
            override fun onResponse(call: Call<ShingraniMember>, response: Response<ShingraniMember>) {

                var message = response.message()
                if (response.isSuccessful){
                    var token = response.body()?.token
                    userManager.saveUser(username, token!!)
                    loginSuccess.value = LoginFormState(isDataValid = true,message = response.body()?.message)

                }else{
                    loginSuccess.value = LoginFormState(isDataValid = false, message = response.message())
                }
            }

            override fun onFailure(call: Call<ShingraniMember>?, t: Throwable?) {
                Log.v("retrofit", "call failed ${t?.message}")
                loginSuccess.value = LoginFormState(isDataValid = false, message = t?.message)
            }
        })
        return loginSuccess
    }


}
