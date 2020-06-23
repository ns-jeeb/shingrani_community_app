package com.locked.shingranicommunity.locked

import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.locked.models.*
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
    fun createEvent(@Body body: EventItem): Call<CreateResponse<EventItem>>

    @DELETE("/api/v2/item/{eventId}")
    fun deleteEvent(@Path("eventId") itemId: String): Call<LockResponse>

    @GET("/api/v2/app/{appid}/item")
    fun getEventList(@Path("appid") appId: String, @Query("template") templateId: String): Call<MutableList<EventItem>>

    @POST("api/v2/urcommunity/rsvp/{itemid}")
    fun accept(@Path("itemid") itemID: String, @Body body: RsvpObject): Call<MutableLiveData<RsvpObject>>

    @GET("/api/v2/app/{appid}/item")
    fun getAnnouncementList(@Path("appid") appId: String, @Query("template") templateId: String): Call<MutableList<AnnouncementItem>>

    @POST("/api/v2/item")
    fun createAnnouncement(@Body body: AnnouncementItem): Call<CreateResponse<AnnouncementItem>>

    @DELETE("/api/v2/item/{announcementId}")
    fun deleteAnnouncement(@Path("announcementId") itemId: String): Call<LockResponse>

    @GET("/api/v2/app/5d4a348f88fb44130084f903/member")
    fun getMembers(@Header("x-access-token") token: String): Call<ArrayList<LockResponse>>

    @POST("/api/v2/app/5d4a348f88fb44130084f903/invite")
    fun inviteMember(@Body email: HashMap<String,String>, @Header("x-access-token") token: String): Call<LockResponse>
}