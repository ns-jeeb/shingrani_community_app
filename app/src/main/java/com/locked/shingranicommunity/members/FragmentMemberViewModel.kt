package com.locked.shingranicommunity.members

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.di.Storage
import javax.inject.Inject

class FragmentMemberViewModel @Inject constructor(var requestMemberApi: MemberApiRequestListener,private val storage: Storage) : ViewModel() {
    private val _emailForm = MutableLiveData<MemberFormState>()
    val emailFormState: LiveData<MemberFormState> = _emailForm

    private val _nameResult = MutableLiveData<InviteResult>()
    val nameResult: LiveData<InviteResult> = _nameResult

    fun inviteMember(email: String,name: String): LiveData<String>{
        var message = MutableLiveData<String>()
        message = requestMemberApi.inviteMember(email, name ,storage.getToken("token"))
        return message
    }

    fun emailDataChanged(email: String, name: String): LiveData<MemberFormState> {
        if (!isEmailValid(email)) {
            _emailForm.value = MemberFormState(emailError = R.string.invalid_username)
        } else if (!isNameValid(name)) {
            _emailForm.value = MemberFormState(nameError = R.string.invalid_password)
        } else {
            _emailForm.value = MemberFormState(isDataValid = true)
        }
        return _emailForm
    }

    private fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private fun isNameValid(name: String): Boolean {
        return name.length>5
    }

    sealed class Result<out T : Any> {

        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()

        override fun toString(): String {
            return when (this) {
                is Success<*> -> "Success[data=$data]"
                is Error -> "Error[exception=$exception]"
            }
        }
    }
    data class MemberFormState(val emailError: Int? = null,
                               val nameError: Int? = null,
                               val isDataValid: Boolean = false)
    data class InviteResult(
        val success: InvitedUserView? = null,
        val error: Int? = null
    )
    data class InvitedUserView(
        val displayName: String
        //... other data fields that may be accessible to the UI
    )

}

