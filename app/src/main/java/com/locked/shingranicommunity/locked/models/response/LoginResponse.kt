package com.locked.shingranicommunity.locked.models.response

import com.locked.shingranicommunity.locked.models.User

data class LoginResponse(
    val token: String,
    val user: User) : LockResponse() {}