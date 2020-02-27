package com.locked.shingranicommunity.dashboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.LockedApiService
import com.locked.shingranicommunity.LockedApiServiceInterface
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.response.DashboardResponseLister
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")
class DashboardRepositor @Inject constructor(private var responseLister: DashboardResponseLister
) : DashboardItemRequestListener {
    override fun deleteFields(itme_id: String,token: String): String? {
        var responseMessage =""
        if (!token.isBlank()){
            responseMessage = deleteItem(itme_id,token)
        }
        return responseMessage
    }


    override fun fetchAnnouncement(template: String): MutableLiveData<ArrayList<Item>>? {

        if (token != null) {
            if (!token.isNullOrBlank()){
                var call = lockedApiService.getItems(template, token!! )
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
        }
        return item
    }

    override fun fetchEvent(template: String): MutableLiveData<ArrayList<Item>>? {
        if (token != null) {
            if (!token.isNullOrBlank()){
                var call = lockedApiService.getItems(template, token!!)
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
        }
        return item
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
    private val sharedPreferences = MyApplication.instance.getSharedPreferences("token", Context.MODE_PRIVATE)
    private var token: String? = sharedPreferences.getString("token","")

    var item: MutableLiveData<ArrayList<Item>>?  =  MutableLiveData()


    fun createEvent(fields:ArrayList<Field>, title: String){


        var eventBodyMap: HashMap<String, Any> = HashMap()

        eventBodyMap["owner"] ="5d428428ab0ef913000dc456"
        eventBodyMap["app"] = "5d4a348f88fb44130084f903"
        eventBodyMap["title"] = title
        eventBodyMap["fields"] = fields

        if (!token.isNullOrBlank()){
            var call = lockedApiService.createEventItem(token!!,eventBodyMap)
            call.enqueue(object : Callback, retrofit2.Callback<Item>{
                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Log.d("Item_Response",t.message)
                }

                override fun onResponse(call: Call<Item>, response: Response<Item>) {

                    Log.d("Item_Response",response.message())
                }
            })
        }

    }
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
}
