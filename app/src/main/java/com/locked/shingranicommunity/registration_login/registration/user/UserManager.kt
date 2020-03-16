
package com.locked.shingranicommunity.registration_login.registration.user

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.di.UserComponent
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject
import javax.inject.Singleton

private const val REGISTERED_USER = "registered_user"
private const val PASSWORD_SUFFIX = "token"

@Singleton
class UserManager @Inject constructor(private val storage: Storage, private val userFactory: UserComponent.Factory) {

    companion object{

    }


    var userComponent: UserComponent? = null
        private set

    val username: String
        get() = storage.getToken(REGISTERED_USER)

    val token: String
        get() = storage.getToken(PASSWORD_SUFFIX)

    fun isUserLoggedIn() = userComponent != null

    fun isUserRegistered() = storage.getToken(REGISTERED_USER).isNotEmpty()

    fun registerUser(username: String, token: String) {
//        storage.setToken(REGISTERED_USER, username)
//        storage.setToken("$username$PASSWORD_SUFFIX", token)
        userJustLoggedIn()
    }
    fun saveUser(username: String, token:String ){
        storage.setToken(REGISTERED_USER, username)
        storage.setToken(PASSWORD_SUFFIX, token)
    }
    fun getUsers(): MutableLiveData<ArrayList<ShingraniMember>> {
        return  storage.getUser()
    }
    fun faildCreateUser(message:String){
//            responseEvent.userCreated(username,message)
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
    fun setCurrentUser(users: User){
        var user: User = users
    }
    fun getCurrentUser(): String{
        return storage.getCurrentUser()
    }

    private fun userJustLoggedIn() {
        userComponent = userFactory.create()

    }
}
