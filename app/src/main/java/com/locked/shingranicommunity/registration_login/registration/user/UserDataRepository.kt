
package com.locked.shingranicommunity.registration_login.registration.user
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.members.LoginResponse
import com.locked.shingranicommunity.models.TemplateModel
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import com.locked.shingranicommunity.registration_login.registration.login.LoginFormState
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback
import kotlin.collections.HashMap

class UserDataRepository @Inject constructor (private val userManager: UserManager): ResponseEvent {
    private var lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)
    val username: String
        get() = userManager.username

    fun login(username: String, password: String) : LiveData<LoginFormState> {
        var loginSuccess = MutableLiveData<LoginFormState>()

        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap["username"]= username
        bodyHashMap["password"]= password

        val call = lockedApiService.userLogin(bodyHashMap,"5d4a348f88fb44130084f903")
        call.enqueue(object : Callback, retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                var message = response.message()
                if (response.isSuccessful){
                    var token = response.body()?.token
                    userManager.saveUser(username, token!!)
                    userManager.setCurrentUser(response.body()?.user!!)
                    loginSuccess.value = LoginFormState(isDataValid = true,message = response.body()?.message)

                }else{
                    var error = parsingJson(response.errorBody() )
                    loginSuccess.value = LoginFormState(isDataValid = false, message = error)
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed ${t?.message}")
                loginSuccess.value = LoginFormState(isDataValid = false, message = t?.message)
            }
        })
        return loginSuccess
    }

    override fun loggedInSuccess(username: String, activity: LoginActivity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun failedLoggedIn(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun userCreated(username: String, password: String,name: String): LiveData<LoginFormState> {
        var bodyHashMap: HashMap<String, String> = HashMap()
        bodyHashMap["username"] = username
        bodyHashMap["password"] = password
        var nameSplit: String
        nameSplit = if (name.isEmpty()) {
            username.split('@')[0]
        }else{
            name
        }
        bodyHashMap["name"] = nameSplit
        var message = MutableLiveData<LoginFormState>()

        val call = lockedApiService.registerUser(bodyHashMap)
        call.enqueue(object : Callback, retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    message.value = LoginFormState(isDataValid = true,message = response.body()?.message)
                }
                else {
                    var error = parsingJson(response.errorBody() )
                    Log.v("retrofitLogin", "call failed $error")
                    message.value = LoginFormState(isDataValid = false, message =error)
                }
            }//203

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                message.value = LoginFormState(isDataValid = false, message =t?.message)
            }
        })

        return message
    }

    override fun failedCreateUser(username: String, error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun itemCreatedMessageDisplay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun fetchedSingleApi() {
        val call = lockedApiService.fetchedSingleApi(userManager.token,"5d4a348f88fb44130084f903")
        call.enqueue(object : Callback, retrofit2.Callback<TemplateModel>{
            override fun onFailure(call: Call<TemplateModel>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<TemplateModel>, response: Response<TemplateModel>) {
                if (response.isSuccessful) {
                    Log.d("SingleApiCall","is success ${response.body()}")
                    userManager.setTemplateModel(response.body()!!)
                }else{
                    Log.d("SingleApiCall","is Failed ${response.body()}")
                }
            }

        })
    }
}
