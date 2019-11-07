package com.locked.shingranicommunity.authenticate.ui.login

/**
 * Data validation state of the login form.
 */
data class AuthenticFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
