package com.locked.shingranicommunity
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.data.Rsvp
import com.locked.shingranicommunity.dashboard.data.RsvpObject
import com.locked.shingranicommunity.members.LoginResponse
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.models.TemplateModel
import retrofit2.Call
import retrofit2.http.*

interface LockedApiServiceInterface {
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
    fun fetchedSingleApi(@Header("x-access-token") token: String,@Path("appid")apiId: String ):Call<TemplateModel>

    @PUT("api/v2/item/{itemid}")
    fun updateItem( @Header("x-access-token") token: String,@Body body: HashMap<String,Any>, @Path("itemid") itemID: String):Call<Item>

    @POST("api/v2/urcommunity/rsvp/{itemid}")
    fun accepted( @Header("x-access-token") token: String,@Body body:RsvpObject, @Path("itemid") itemID: String): Call<RsvpObject>

    @POST("api/v2/urcommunity/rsvp/{itemid}")
    fun rejected( @Header("x-access-token") token: String,@Body body:RsvpObject, @Path("itemid") itemID: String): Call<RsvpObject>

}