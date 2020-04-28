package com.locked.shingranicommunity.models

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