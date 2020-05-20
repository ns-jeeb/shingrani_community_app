package com.locked.shingranicommunity.locked.models

data class ErrorMessage(
    val message: String,
    val errorType: String,
    val errors: List<Error>
)