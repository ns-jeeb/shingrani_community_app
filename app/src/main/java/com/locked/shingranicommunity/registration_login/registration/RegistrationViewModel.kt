

package com.locked.shingranicommunity.registration_login.registration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.registration_login.registration.user.UserDataRepository
import javax.inject.Inject

/**
 * RegistrationViewModel is the ViewModel that the Registration flow ([RegistrationActivity]
 * and fragments) uses to keep user's input data.
 */

class RegistrationViewModel @Inject constructor(val context: Context): ViewModel() {


    private var username: String? = null
    private var password: String? = null
    private var name: String? = null
    private var message: LiveData<String>? = null
    private var acceptedTCs: Boolean? = null

    @Inject
    lateinit var repos: UserDataRepository
//    lateinit var repos: RegisterRepository

    fun updateUserData(username: String, password: String,name: String) {
        this.username = username
        this.password = password
        this.name = name
    }

    fun acceptTCs() {
        acceptedTCs = true
    }
//    fun registrationFailed(): String{
//        return message!!
//    }

    fun registerUser() {
        assert(username != null)
        assert(password != null)
        assert(name != null)
        assert(message != null)
        assert(acceptedTCs == true)
    }
}
