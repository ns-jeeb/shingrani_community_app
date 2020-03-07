

package com.locked.shingranicommunity.registration_login.registration.enterdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.registration_login.registration.login.LoginFormState
import com.locked.shingranicommunity.registration_login.registration.user.UserDataRepository
import javax.inject.Inject

const val MAX_LENGTH = 5

/**
 * EnterDetailsViewModel is the ViewModel that [EnterDetailsFragment] uses to
 * obtain to validate user's input data.
 */
class EnterDetailsViewModel @Inject constructor(var repository: UserDataRepository) {

    private val _enterDetailsState = MutableLiveData<EnterDetailsViewState>()
    val enterDetailsState: LiveData<EnterDetailsViewState>
        get() = _enterDetailsState

    fun registerUser(username: String, password: String,name: String): LiveData<LoginFormState>{
        return repository.userCreated(username,password,name)
    }
    fun validateInput(username: String, password: String,conformPassword: String,name: String) {
        when {
            username.length < MAX_LENGTH -> _enterDetailsState.value =
                EnterDetailsError("Username has to be longer than 4 characters")
            password.length < MAX_LENGTH -> _enterDetailsState.value =
                EnterDetailsError("Password has to be longer than 4 characters")
            !password.contentEquals(conformPassword) ->_enterDetailsState.value = EnterDetailsError("password is not match")
            else -> {

                _enterDetailsState.value = EnterDetailsSuccess
            }
        }
    }
}
