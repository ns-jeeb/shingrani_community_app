package com.locked.shingranicommunity.locked.models.response

import com.locked.shingranicommunity.locked.models.Error

data class RegisterResponse(
        val message: String,
        val errorType: String,
        val errors: List<Error>
){
    fun hasError() : Boolean {
        return errorType != null || !errors.isEmpty()
    }
}