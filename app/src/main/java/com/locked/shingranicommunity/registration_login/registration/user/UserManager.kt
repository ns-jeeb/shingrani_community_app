
package com.locked.shingranicommunity.registration_login.registration.user

import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.di.UserComponent
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.models.Admin
import com.locked.shingranicommunity.models.TemplateModel
import javax.inject.Inject
import javax.inject.Singleton

private const val REGISTERED_USER = "registered_user"
private const val PASSWORD_SUFFIX = "token"

@Singleton
class UserManager @Inject constructor(val storage: Storage, private val userFactory: UserComponent.Factory) {
    companion object userStorage{
        var memeberUser = MutableLiveData<ArrayList<ShingraniMember>>()
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
//    fun getUsers(): MutableLiveData<ArrayList<ShingraniMember>> {
//        memeberUser = storage.getUser()
//        return storage.getUser()
//    }

    fun loginUser(): Boolean {
        if (getCurrentUser() == null){
            storage.setToken("token","")
        }
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
        storage.setCurrentUser(users)
    }
    fun getCurrentUser(): User?{
        return storage.getCurrentUser()
    }

    private fun userJustLoggedIn() {
        userComponent = userFactory.create()

    }

    fun getAdminUser(id: String): Admin? {
        if (getTemplateModel() != null){
            for (i in 0 until getTemplateModel()?.admins?.size!!){
                if (id == getTemplateModel()?.admins?.get(i)?._id){
                    return getTemplateModel()?.admins?.get(i)
                }
            }
        }
        return null
    }


    fun getTemplateModel(): TemplateModel?{
        return storage.getTemplateModel()
    }
    fun setTemplateModel(templateModel: TemplateModel){
        storage.setTemplateModel(templateModel)
    }
}
