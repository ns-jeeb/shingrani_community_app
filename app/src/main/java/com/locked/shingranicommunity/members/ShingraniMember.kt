package com.locked.shingranicommunity.members

import com.locked.shingranicommunity.models.Error
import com.locked.shingranicommunity.models.User

data class ShingraniMember(
    val __v: Int,
    val _id: String,
    val app: String,
    val email: String,
    val state: String,
    var user: User?,
    var message: String,
    val token: String,
    var errorType: String ="",
    var errors: List<Error>?
){
    fun setError(error: String){
        message = error
    }
    fun hasError() : Boolean {
        return errorType != null || !errors?.isEmpty()!!
    }
}

data class SuccessMessage(
        val message: String,
        val user_id: String
)



