package com.locked.shingranicommunity

import com.locked.shingranicommunity.storage.model.LoggedInUser
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.tutorials.RegisterUser
import retrofit2.Call
import retrofit2.http.*

interface LockedApiServiceInterface {
    @POST("/api/v2/login")
    fun userLogin(@Body body: Map<String, String>): Call<LoggedInUser>

    @Headers("Content-Type: application/json")
    @POST("/api/v2/register")
    fun registerUser(@Body body: Map<String, String>): Call<RegisterUser>

    @GET("/api/v2/app/5d4a348f88fb44130084f903/item")
    fun getItems(@Query("template") template: String ,@Header("x-access-token") token: String): Call<ArrayList<Item>>

    @GET("/api/v2/app/5d4a348f88fb44130084f903/item")
    fun deleteItems(@Header("x-access-token") token: String,itemID:String):Call<String>

    @POST("/api/v2/item")
    fun createEventItem(@Header("x-access-token") token: String,@Body body:HashMap<String , Any>): Call<Item>

}