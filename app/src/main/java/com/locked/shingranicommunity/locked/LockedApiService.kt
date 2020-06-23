package com.locked.shingranicommunity.locked

import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.locked.models.*
import com.locked.shingranicommunity.locked.models.request.*
import com.locked.shingranicommunity.locked.models.response.CreateResponse
import com.locked.shingranicommunity.locked.models.response.LockResponse
import com.locked.shingranicommunity.locked.models.response.LoginResponse
import com.locked.shingranicommunity.locked.models.response.RegisterResponse
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
    fun accept(@Path("itemid") itemID: String, @Body body: RsvpRequestBody): Call<MutableLiveData<RsvpRequestBody>>

    @GET("/api/v2/app/{appid}/item")
    fun getAnnouncementList(@Path("appid") appId: String, @Query("template") templateId: String): Call<MutableList<AnnouncementItem>>

    @POST("/api/v2/item")
    fun createAnnouncement(@Body body: AnnouncementItem): Call<CreateResponse<AnnouncementItem>>

    @DELETE("/api/v2/item/{announcementId}")
    fun deleteAnnouncement(@Path("announcementId") itemId: String): Call<LockResponse>

    @GET("/api/v2/app/{appid}/member")
    fun getMemberList(@Path("appid") appId: String): Call<MutableList<Member>>

    @POST("/api/v2/app/{appid}/invite")
    fun inviteMember(@Path("appid") appId: String, @Body body: InviteRequestBody): Call<LockResponse>

    @POST("/api/v2/app/{appid}/state/{memberid}")
    fun blockMember(@Path("appid") appId: String, @Path("memberid") memberId: String, @Body body: BlockRequestBody = BlockRequestBody()): Call<LockResponse>
}