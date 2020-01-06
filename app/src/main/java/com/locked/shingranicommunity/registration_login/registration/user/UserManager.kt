
package com.locked.shingranicommunity.registration_login.registration.user

import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.di.UserComponent
import javax.inject.Inject
import javax.inject.Singleton

private const val REGISTERED_USER = "registered_user"
private const val PASSWORD_SUFFIX = "password"

/**
 * Handles User lifecycle. Manages registrations, logs in and logs out.
 * Knows when the user is logged in.
 */
@Singleton
class UserManager @Inject constructor(private val storage: Storage,private val userFactory: UserComponent.Factory) {

    /**
     *  UserDataRepository is specific to a logged in user. This determines if the user
     *  is logged in or not, when the user logs in, a new instance will be created.
     *  When the user logs out, this will be null.
     */
    var userComponent: UserComponent? = null
        private set

    val username: String
        get() = storage.getToken(REGISTERED_USER)

    fun isUserLoggedIn() = userComponent != null

    fun isUserRegistered() = storage.getToken(REGISTERED_USER).isNotEmpty()

    fun registerUser(username: String, password: String) {
        storage.setToken(REGISTERED_USER, username)
        storage.setToken("$username$PASSWORD_SUFFIX", password)
        userJustLoggedIn()
    }

    fun loginUser(username: String, password: String): Boolean {
        val registeredUser = this.username
        if (registeredUser.isBlank()){
            return false
        }

        val registeredPassword = storage.getToken("$username$PASSWORD_SUFFIX")
        if (registeredPassword != password) return false

        userJustLoggedIn()
        return true
    }

    fun logout() {
        userComponent = null
    }

    fun unregister() {
        val username = storage.getToken(REGISTERED_USER)
        storage.setToken(REGISTERED_USER, "")
        storage.setToken("$username$PASSWORD_SUFFIX", "")
        logout()
    }

    private fun userJustLoggedIn() {
        userComponent = userFactory.create()
    }
}
