

package com.locked.shingranicommunity.registration_login.registration

import android.content.Context
import android.widget.Toast
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.di.scops.ActivityScope
import com.locked.shingranicommunity.registration_login.registration.user.RegisterRepository
import com.locked.shingranicommunity.registration_login.registration.user.UserDataRepository
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject

/**
 * RegistrationViewModel is the ViewModel that the Registration flow ([RegistrationActivity]
 * and fragments) uses to keep user's input data.
 */
@ActivityScope
class RegistrationViewModel @Inject constructor(val userManager: UserManager,val context: Context) {


    private var username: String? = null
    private var password: String? = null
    private var name: String? = null
    var message: String? = null
    private var acceptedTCs: Boolean? = null

    @Inject
    lateinit var repos: RegisterRepository

    fun updateUserData(username: String, password: String,name: String) {
        this.username = username
        this.password = password
        this.name = name
    }

    fun acceptTCs() {
        acceptedTCs = true
    }

    fun registerUser() {
        assert(username != null)
        assert(password != null)
        assert(name != null)
        assert(message != null)
        assert(acceptedTCs == true)
        message = repos.register(username!!,password!!,name!!)
    }
}
