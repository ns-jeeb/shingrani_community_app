package com.locked.shingranicommunity.dashboard.data

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




data class Users(
    @SerializedName("token")
    @Expose
    var token: String,
    @SerializedName("_id")
    @Expose
    var id: String,
    @SerializedName("role")
    @Expose
    private var role: String? = null,
    @SerializedName("keywords")
    @Expose
    private var keywords: List<String?>? = null,
    @SerializedName("picture")
    @Expose
    private var picture: String? = null,
    @SerializedName("f_name")
    @Expose
    var fName: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("password")
    @Expose
    var password: String? = null,
    @SerializedName("l_name")
    @Expose
    var lName: String? = null,
    @SerializedName("user_comm_id")
    @Expose
    private var userCommId: String? = null,
    @SerializedName("address")
    @Expose
    var address: String? = null,
    @SerializedName("city")
    @Expose
    var city: String? = null,
    @SerializedName("province")
    @Expose
    var province: String? = null,
    @SerializedName("postal_code")
    @Expose
    var postalCode: String? = null,
    @SerializedName("home_phone")
    @Expose
    var homePhone: String? = null,
    @SerializedName("cell_phone")
    @Expose
    var cellPhone: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: String? = null,
    @SerializedName("age")
    @Expose
    private var age: String? = null,
    //    @SerializedName("createdAt")
//    @Expose
//    private CreatedAt createdAt;
//    @SerializedName("updatedAt")
//    @Expose
//    private UpdatedAt updatedAt;
    @SerializedName("__v")
    @Expose
    private var v: Int? = null
){}

