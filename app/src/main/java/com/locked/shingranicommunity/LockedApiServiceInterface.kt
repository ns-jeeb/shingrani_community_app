package com.locked.shingranicommunity
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.members.LoginResponse
import com.locked.shingranicommunity.members.ShingraniMember
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

    @GET("/api/v2/app/5d4a348f88fb44130084f903/item")
    fun deleteItems(@Header("x-access-token") token: String,itemID:String):Call<String>

    @POST("/api/v2/item")
    fun createEventItem(@Header("x-access-token") token: String,@Body body:HashMap<String , Any>): Call<Item>

    @GET("/api/v2/app/5d4a348f88fb44130084f903/member")
    fun getMembers(@Header("x-access-token") token: String): Call<ArrayList<ShingraniMember>>

    @POST("/api/v2/app/5d4a348f88fb44130084f903/invite")
    fun inviteMember(@Body email: HashMap<String,String>, @Header("x-access-token") token: String): Call<ShingraniMember>

    @PUT("api/v2/item/{itemid}")
    fun updateItem( @Header("x-access-token") token: String,@Body body: HashMap<String,Any>, @Path("itemid") itemID: String): Call<Item>

}