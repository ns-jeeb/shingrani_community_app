package com.locked.shingranicommunity.storage.model

//import com.locked.shingranicommunity.tutorials.User

/**
 * Data class that captures user information for logged in users retrieved from AuthenticationRepository
 */
data class LoggedInUser(
    val token: String
)
