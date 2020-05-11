package com.locked.shingranicommunity.locked

import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.locked.models.LockResponse
import com.locked.shingranicommunity.locked.models.LoginRequestBody
import com.locked.shingranicommunity.locked.models.RegisterRequestBody
import com.locked.shingranicommunity.locked.models.RsvpObject
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.models.*
import retrofit2.Call
import retrofit2.http.*

interface LockedApiService {

    @POST("/api/v2/login")
    @Headers("Content-Type: application/json")
    fun login(@Body body: LoginRequestBody): Call<LoginResponse>

    @POST("/api/v2/register")
    @Headers("Content-Type: application/json")
    fun register(@Body body: RegisterRequestBody): Call<RegisterResponse>

    @GET("/api/v2/app/{appid}")
    fun app(@Path("appid") appId: String): Call<AppModel>

    @POST("/api/v2/item")
    fun createEvent(@Body body: EventItem): Call<EventItem>

    @DELETE("/api/v2/item/{eventId}")
    fun deleteEvent(@Path("eventId") itemId: String): Call<LockResponse>

    @GET("/api/v2/app/{appid}/item")
    fun getEventList(@Path("appid") appId: String, @Query("template") templateId: String): Call<MutableList<EventItem>>

    @POST("/api/v2/login")
    @Headers("Content-Type: application/json")
    fun userLogin(@Body body: Map<String, String>, @Header("appid")apiId: String): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/v2/register")
    fun registerUser(@Body body: Map<String, String>): Call<LoginResponse>

    @GET("/api/v2/app/5d4a348f88fb44130084f903/item")
    fun getItems(@Query("template") template: String ,@Header("x-access-token") token: String): Call<ArrayList<Item>>

    @DELETE("/api/v2/item/{itemid}")
    fun deleteItems(@Header("x-access-token") token: String,@Path(value = "itemid",encoded = true)itemId: String ):Call<Item>

    @POST("/api/v2/item")
    fun createEventItem(@Header("x-access-token") token: String,@Body body:HashMap<String , Any>): Call<Item>

    @POST("/api/v2/item")
    fun createAnnounce(@Header("x-access-token") token: String,@Body body:HashMap<String , Any>): Call<Item>

    @GET("/api/v2/app/5d4a348f88fb44130084f903/member")
    fun getMembers(@Header("x-access-token") token: String): Call<ArrayList<ShingraniMember>>

    @POST("/api/v2/app/5d4a348f88fb44130084f903/invite")
    fun inviteMember(@Body email: HashMap<String,String>, @Header("x-access-token") token: String): Call<ShingraniMember>
    @GET("/api/v2/app/{appid}")
    fun fetchedSingleApi(@Header("x-access-token") token: String,@Path("appid")apiId: String ):Call<AppModel>

    @PUT("api/v2/item/{itemid}")
    fun updateItem( @Header("x-access-token") token: String,@Body body: HashMap<String,Any>, @Path("itemid") itemID: String):Call<Item>

    @POST("api/v2/urcommunity/rsvp/{itemid}")
    fun accepted(@Body body: RsvpObject, @Path("itemid") itemID: String): Call<MutableLiveData<RsvpObject>>

    @POST("api/v2/urcommunity/rsvp/{itemid}")
    fun rejected(@Body body: RsvpObject, @Path("itemid") itemID: String): Call<MutableLiveData<RsvpObject>>

}