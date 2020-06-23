package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.auth.AuthConstants
import com.locked.shingranicommunity.di.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.Error
import com.locked.shingranicommunity.locked.models.Member
import com.locked.shingranicommunity.locked.models.request.InviteRequestBody
import com.locked.shingranicommunity.locked.models.response.LockResponse
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

@AppScope
class MemberRepository @Inject constructor(
    val apiService: LockedApiService,
    val session: Session) {

    private var loading: Boolean = false
    private val _fetchMembers: MutableLiveData<Data> = MutableLiveData<Data>()
    private val _inviteMember: MutableLiveData<Data> = MutableLiveData<Data>()
    private val _blockMember: MutableLiveData<Data> = MutableLiveData<Data>()
    private val _authError: MutableLiveData<Boolean> = MutableLiveData(false)
    val fetchMembers: LiveData<Data> = _fetchMembers
    val inviteMember: LiveData<Data> = _inviteMember
    val blockMember: LiveData<Data> = _blockMember
    val authError: LiveData<Boolean> = _authError
    var members: MutableList<Member> = mutableListOf()

    init {
        session.loginState.observeForever(Observer { _authError.value = !it })
        refreshMembers()
    }

    fun fetchMembers() {
       if (members.isEmpty()) {
           refreshMembers()
       } else {
           _fetchMembers.postValue(Data(true))
       }
    }

    private fun refreshMembers() {
        if (!loading) {
            loading = true
            apiService.getMemberList(session.getAppId()).enqueue(FetchMembersListener())
        }
    }

    fun fetchMember(memberId: String, callback: ((Member) -> Unit)) {
        val foundMember = members.find { it._id == memberId }
        if (foundMember != null) {
            callback.invoke(foundMember)
        }
    }

    fun inviteMember(email: String) {
        apiService.inviteMember(session.getAppId(), InviteRequestBody(email))
            .enqueue(InviteMemberListener(email))
    }

    fun blockMember(member: Member) {
        apiService.blockMember(session.getAppId(), member._id)
            .enqueue(BlockMemberListener(member))
    }

    private fun checkForTokenError(details: List<Error>) {
        details.forEach {
            if (it.code == AuthConstants.TOKEN_ERROR) {
                _authError.postValue(true)
            }
        }
    }

    private inner class FetchMembersListener(): LockedCallback<MutableList<Member>>() {
        override fun success(response: MutableList<Member>) {
            members.clear()
            members.addAll(response)
            loading = false
            _fetchMembers.postValue(Data(true))
        }
        override fun fail(message: String, details: List<Error>) {
            loading = false
            checkForTokenError(details)
            _fetchMembers.postValue(Data(false))
        }
    }

    private inner class InviteMemberListener(val email: String): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            refreshMembers()
            _inviteMember.postValue(Data(true, response.message))
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            _inviteMember.postValue(Data(false, message))
        }
    }

    private inner class BlockMemberListener(val member: Member): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            refreshMembers()
            _blockMember.postValue(Data(true, response.message))
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            _blockMember.postValue(Data(false, message))
        }
    }

    data class Data(var success: Boolean = false,
                    var message: String? = null)
}