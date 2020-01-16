
package com.locked.shingranicommunity.registration_login.registration.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.di.UserComponent
import com.locked.shingranicommunity.registration_login.registration.login.LoginError
import com.locked.shingranicommunity.registration_login.registration.login.LoginSuccess
import com.locked.shingranicommunity.registration_login.registration.login.LoginViewState
import javax.inject.Inject
import javax.inject.Singleton

private const val REGISTERED_USER = "registered_user"
private const val PASSWORD_SUFFIX = "token"

/**
 * Handles User lifecycle. Manages registrations, logs in and logs out.
 * Knows when the user is logged in.
 */
@Singleton
class UserManager @Inject constructor(private val storage: Storage,private val userFactory: UserComponent.Factory, private val responseEvent: ResponseEvent) {

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

    fun registerUser(username: String, token: String) {
//        storage.setToken(REGISTERED_USER, username)
//        storage.setToken("$username$PASSWORD_SUFFIX", token)
        userJustLoggedIn()
    }
    fun saveUser(username: String, token:String){
        storage.setToken(REGISTERED_USER, username)
        storage.setToken(PASSWORD_SUFFIX, token)
        responseEvent.loggedInSuccess(username)
    }
    fun userCreated(message:String){
        responseEvent.userCreated(username,message)
    }
    fun faildCreateUser(message:String){
            responseEvent.userCreated(username,message)
    }

    fun loginUser(): Boolean {

        val registeredUser = storage.getToken(REGISTERED_USER)
        if (registeredUser.isBlank()){
            return false
        }

        val registeredPassword = storage.getToken(PASSWORD_SUFFIX)
        if (registeredPassword.isBlank()) return false

        userJustLoggedIn()
        return true
    }

    fun logout() {
        userComponent = null
    }

    fun unregister() {
        val username = storage.getToken(REGISTERED_USER)
        storage.setToken(REGISTERED_USER, "")
        storage.setToken(PASSWORD_SUFFIX, "")
        logout()
    }

    private fun userJustLoggedIn() {
        userComponent = userFactory.create()

    }
}
