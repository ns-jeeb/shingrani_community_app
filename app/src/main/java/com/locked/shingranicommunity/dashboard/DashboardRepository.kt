package com.locked.shingranicommunity.dashboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.*
import com.locked.shingranicommunity.dashboard.data.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")
class DashboardRepositor(private val ioDispatcher: CoroutineDispatcher) : I_FetchedEventAnnouncements {
    override fun fetchAnnouncement(template: String): LiveData<List<Item>>? {
        loadAnnouncement(template)
        if (!item?.value.isNullOrEmpty()){
            return item?.value as? LiveData<List<Item>>
        }else{
            item?.value?.add(Item())
            return null
        }
    }

    override fun fetchEvent(template: String): LiveData<List<Item>>? {
        loadEvent(template)
        if (!item?.value.isNullOrEmpty()){
            return item?.value as? LiveData<List<Item>>
        }else{
            item?.value?.add(Item())
            return null
        }
    }

    override var cachedData= MutableLiveData<List<Item>>()


    override fun getFields(): LiveData<List<Item>>?{
        Log.d("Items_Event","test")
        return cachedData
    }

    override suspend fun fetchNewItem() {
        withContext(Dispatchers.Main) {
        }
    }

    private lateinit var lockedApiService: LockedApiServiceInterface

    var item: MutableLiveData<ArrayList<Item>>? =  MutableLiveData()
    fun loadEvent(template: String){

        lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)!!
        val sharedPreferences = CommunityApp.instance.getSharedPreferences("token", Context.MODE_PRIVATE)
        val token: String? = sharedPreferences.getString("token","")

        if (token != null) {
            if (!token.isNullOrBlank()){
                var call = lockedApiService.getItems(template, token )
                call.enqueue(object :Callback, retrofit2.Callback<ArrayList<Item>>{
                    override fun onResponse(call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                       Log.d("Item_Response","response is working")

                        if (response.isSuccessful && item != null) {
                            item!!.postValue(response.body())
                            cachedData.value = response.body()
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                        Toast.makeText(CommunityApp.instance,"response Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

    }

    fun loadAnnouncement(template: String){

        lockedApiService = LockedApiService().getClient().create(LockedApiServiceInterface::class.java)!!
        val sharedPreferences = CommunityApp.instance.getSharedPreferences("token", Context.MODE_PRIVATE)
        val token: String? = sharedPreferences.getString("token","")

        if (token != null) {
            if (!token.isNullOrBlank()){
                var call = lockedApiService.getItems(template, token )
                call.enqueue(object :Callback, retrofit2.Callback<ArrayList<Item>>{
                    override fun onResponse(call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                        Log.d("Item_Response","response is working")

                        if (response.isSuccessful && item != null) {
                            item!!.postValue(response.body())
                            cachedData.value = response.body()

                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                        Toast.makeText(CommunityApp.instance,"response Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

    }

    private var counter = 0
    // Using ioDispatcher because the function simulates a long and expensive operation.
    private suspend fun simulateNetworkDataFetch(): String = withContext(ioDispatcher) {
        counter++
        "New data from request #$counter"
    }
}
