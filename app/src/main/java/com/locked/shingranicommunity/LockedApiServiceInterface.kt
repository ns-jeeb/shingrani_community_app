package com.locked.shingranicommunity

import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.http.*

interface LockedApiServiceInterface {
    @POST("/api/v2/login")
    fun userLogin(@Body body: Map<String, String>): Call<LoggedInUser>

//    @Headers("Accept: application/json")
//    @POST("users")
//    abstract fun createMember(@Body body: Any): Call<User>
//
//    @Headers("Accept: application/json")
//    @GET("users/me")
//    abstract fun getCurrentUser(@Header("Authorization") token: String): Call<User>
//
//    @GET("users")
//    @Headers("Content-Type: application/json")
//    abstract fun getUsers(@Header("Authorization") token: String): Call<List<User>>
//
//    @Headers("Accept: application/json")
//    @POST("communities")
//    abstract fun createCommunity(@Body body: Map<String, String>): Call<Community>
//
//    @Headers("Accept: application/json")
//    @GET("communities")
//    abstract fun getCommunities(@Header("Authorization") token: String): Call<Community>
//
//    @Headers("Accept: application/json")
//    @POST("invitations")
//    abstract fun createInvitations(@Header("Authorization") token: String, @Body body: Map<String, Any>): Call<Invitation>
//
//    @Headers("Accept: application/json")
//    @GET("invitations")
//    abstract fun getInvitations(@Header("Authorization") token: String): Call<InvitationRow>
}