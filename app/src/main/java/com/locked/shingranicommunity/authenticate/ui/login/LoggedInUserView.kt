package com.locked.shingranicommunity.authenticate.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val userName: String,
    val userId: String
)
