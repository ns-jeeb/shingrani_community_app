package com.locked.shingranicommunity.authenticate.data.model

import com.locked.shingranicommunity.tutorials.User

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val user: User,
    val token: String
)
