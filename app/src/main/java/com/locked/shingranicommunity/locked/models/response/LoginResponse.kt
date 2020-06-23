package com.locked.shingranicommunity.locked.models.response

import com.locked.shingranicommunity.locked.models.Error
import com.locked.shingranicommunity.locked.models.User

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User,
    val errorType: String,
    val errors: List<Error>
){
    fun hasError() : Boolean {
        return errorType != null || !errors.isEmpty()
}

}