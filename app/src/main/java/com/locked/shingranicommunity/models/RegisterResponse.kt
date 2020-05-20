package com.locked.shingranicommunity.models

data class RegisterResponse(
        val message: String,
        val errorType: String,
        val errors: List<Error>
){
    fun hasError() : Boolean {
        return errorType != null || !errors.isEmpty()
    }
}