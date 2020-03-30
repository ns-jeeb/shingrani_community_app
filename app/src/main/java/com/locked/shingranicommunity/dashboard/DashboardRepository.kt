package com.locked.shingranicommunity.dashboard

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.Constant_Utils.ANNOUNCEMENT_TIMPLATE_ID
import com.locked.shingranicommunity.Constant_Utils.APP_ID
import com.locked.shingranicommunity.Constant_Utils.EVENT_TIMPLATE_ID
import com.locked.shingranicommunity.Constant_Utils.OWNER_ID
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.response.DashboardResponseLister
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import com.locked.shingranicommunity.utail.AuthResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")
class DashboardRepositor @Inject constructor(private var responseLister: DashboardResponseLister,var userManager: UserManager
) : DashboardItemRequestListener {
    override fun deleteFields(itme_id: String,token: String): String? {
        var responseMessage =""
        if (!token.isBlank()){
            responseMessage = deleteItem(itme_id,token)
        }
        return responseMessage
    }


    override fun fetchAnnouncement(template: String): MutableLiveData<ArrayList<Item>>? {

            if (!userManager.token.isBlank()){
                var call = lockedApiService.getItems(template, userManager.token)
                call.enqueue(object :Callback, retrofit2.Callback<ArrayList<Item>>{
                    override fun onResponse(call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                        Log.d("Item_Response","response is working")

                        if (response.isSuccessful && item != null) {
                            item!!.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                        Toast.makeText(MyApplication.instance,"response Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }

        return item
    }

    override fun createAnnouncement(fields: ArrayList<Field>): MutableLiveData<String> {
        var body: HashMap<String, Any> = HashMap()
        var string = MutableLiveData<String>()
        body["owner"] = OWNER_ID
        body["app"] = APP_ID
        body["template"] = ANNOUNCEMENT_TIMPLATE_ID
        body["title"] = "Solgira Mockup"
        body["fields"] = fields

        var call = lockedApiService.createAnnounce(userManager.token,body)
        call.enqueue(object : Callback, retrofit2.Callback<Item>{
            override fun onFailure(call: Call<Item>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                Log.d("Response_createAnnouncement","${response.body()}")
            }

        })
        return string
    }

    override fun createEvent(fields: ArrayList<Field>):  MutableLiveData<String> {

        var message : MutableLiveData<String> = MutableLiveData()
        var eventBodyMap: HashMap<String, Any> = HashMap()

//        eventBodyMap["_id"] ="5d770cd8ea2f6b1300f03ca7"
        eventBodyMap["owner"] = OWNER_ID
        eventBodyMap["app"] = APP_ID
        eventBodyMap["template"] = EVENT_TIMPLATE_ID
        eventBodyMap["title"] = "Solgira Mockup"
        eventBodyMap["fields"] = fields

        if (!userManager.token.isBlank()){
            var call = lockedApiService.createEventItem(userManager.token,eventBodyMap)
            call.enqueue(object : Callback, retrofit2.Callback<Item>{
                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Log.d("Item_Response",t.message)
                }

                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    if (response.isSuccessful) {
                        message.value = response.message()
                    }
                    Log.d("Item_Response",response.message())
                }
            })
        }
        return message
    }


    override fun fetchEvent(template: String): MutableLiveData<ArrayList<Item>>? {

            if (!userManager.token.isBlank()){
                var call = lockedApiService.getItems(template, userManager.token!!)
                call.enqueue(object :Callback, retrofit2.Callback<ArrayList<Item>>{
                    override fun onResponse(call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                        Log.d("Item_Response","response is working")

                        if (response.isSuccessful && item != null) {
                            item!!.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                        Toast.makeText(MyApplication.instance,"response Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }

        return item
    }

    override fun updateItem(fields: ArrayList<Field>?, itemId: String?) : String{
        var message : MutableLiveData<String> = MutableLiveData()
        var eventBodyMap: HashMap<String, Any> = HashMap()
        eventBodyMap["fields"] = fields!!
        var call = lockedApiService.updateItem(userManager.token,eventBodyMap, itemId!!)
        call.enqueue(object : Callback, retrofit2.Callback<Item>{
            override fun onFailure(call: Call<Item>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    Log.d("update_response","value of my Response ${response.body()}")
                }else{

                    var autherror = AuthResource.error("message",response.errorBody())
                    Log.d("update_response","value of my Error Response ${autherror.status}")
                    Log.d("update_response","value of my Error Response ${autherror.data}")
                    Log.d("update_response","value of my Error Response ${autherror.message}")
                }
            }

        })
        return ""
    }

    override var cachedData= MutableLiveData<ArrayList<Item>>()


    override fun getFields(): LiveData<ArrayList<Item>>?{
        Log.d("Items_Event","test")
        return cachedData
    }

    override suspend fun fetchNewItem() {
        withContext(Dispatchers.Main) {
        }
    }
    private var lockedApiService: LockedApiServiceInterface = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)!!
//    private val sharedPreferences = MyApplication.instance.getSharedPreferences("token", Context.MODE_PRIVATE)

    var item: MutableLiveData<ArrayList<Item>>?  =  MutableLiveData()
    fun deleteItem(itme_id: String,adminToken: String): String{

        var responseMess = ""
        if (!adminToken.isBlank()){
            var call = lockedApiService.deleteItems(adminToken,itme_id)
            if (!itme_id.isBlank()){
                call.enqueue(object : Callback, retrofit2.Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        responseMess = t.message.toString()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("Item_deleted",response.message())
                        responseMess = response.message()
                    }

                })
            }
        }
        return responseMess
    }
//    fun parsingGson(errorBody: ResponseBody){
//        val gson = Gson()
//        var mydata = errorBody
//        mydata = gson.fromJson(mydata.toString(),ErrorBodyResponse1::class)
//    }
}
