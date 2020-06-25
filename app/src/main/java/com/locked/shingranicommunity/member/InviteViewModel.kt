package com.locked.shingranicommunity.member

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.repositories.MemberRepository
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class InviteViewModel @Inject constructor(
    private val repository: MemberRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val res: ResourceProvider) : ViewModel() {

    private val data: Data = Data()
    private val validation = DataValidation()

    val pageTitle: String = res.getString(R.string.invite_member)
    val email: LiveData<String> = data.email
    val message: LiveData<String> = data.message
    var isFormValid: Boolean = validation.isValid()

    val isEmailValid: LiveData<String> = validation.isEmailValid

    init {
        repository.authError.observeForever(Observer { authErrorOccurred(it) })
    }

    private fun authErrorOccurred(it: Boolean?) {
        it?.let {
            if (it) {
                navigation.navigateToLogin(true)
            }
        }
    }

    fun setEmail(email: String) {
        data.email.postValue(email)
    }

    fun messageHandled() {
        data.message.postValue(null)
    }
    
    fun invite() {
        if (!session.isUserAdmin()) {
            data.message.postValue(res.getString(R.string.must_be_admin))
            return
        }
        validateForm()
        if (isFormValid) {
            repository.inviteMember(email.value!!) {
                if (it.success) {
                    // invite success
                    navigation.inviteFinished()
                } else {
                    data.message.postValue(it.message)
                }
            }
        }
    }

    private fun validateForm() {
        if (email.value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()) {
            validation.isEmailValid.value = res.getString(R.string.validation_email)
        } else {
            validation.isEmailValid.value = null
        }
        isFormValid = validation.isValid()
    }

    private class Data(val email: MutableLiveData<String> = MutableLiveData(""),
                       val message: MutableLiveData<String> = MutableLiveData<String>()) {}

    private data class DataValidation(
        val isEmailValid: MutableLiveData<String> = MutableLiveData()) {
        fun isValid(): Boolean {
            return isEmailValid.value == null
        }
    }
}
