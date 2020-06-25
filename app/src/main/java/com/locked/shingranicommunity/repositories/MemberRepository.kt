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
import com.locked.shingranicommunity.locked.models.MemberState
import com.locked.shingranicommunity.locked.models.User
import com.locked.shingranicommunity.locked.models.request.InviteRequestBody
import com.locked.shingranicommunity.locked.models.response.LockResponse
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

@AppScope
class MemberRepository @Inject constructor(
    val apiService: LockedApiService,
    val session: SessionManager) {

    private var loading: Boolean = false
    private val _fetchMembers: MutableLiveData<Data> = MutableLiveData<Data>()
    private val _authError: MutableLiveData<Boolean> = MutableLiveData(false)
    val fetchMembers: LiveData<Data> = _fetchMembers
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

    private inner class FetchMembersListener(): LockedCallback<MutableList<Member>>() {
        override fun success(response: MutableList<Member>) {
            val admins: List<User> = session.getAdminList()
            admins.forEach {
                response.add(0, Member("-1", session.getAppId(), it.username, MemberState.JOINED.state, it))
            }
            val loadedMembers = response.distinctBy { it.email }
            loadedMembers.forEach { member ->
                member.user?.let { member.isAdmin = session.isAdmin(it) }
            }
            members.clear()
            members.addAll(loadedMembers)
            loading = false
            _fetchMembers.postValue(Data(true))
        }
        override fun fail(message: String, details: List<Error>) {
            loading = false
            checkForTokenError(details)
            _fetchMembers.postValue(Data(false))
        }
    }

    fun fetchMember(memberId: String, callback: ((Member) -> Unit)) {
        val foundMember = members.find { it._id == memberId }
        if (foundMember != null) {
            callback.invoke(foundMember)
        }
    }

    fun inviteMember(email: String, callback: (Data) -> Unit) {
        apiService.inviteMember(session.getAppId(), InviteRequestBody(email))
            .enqueue(InviteMemberListener(email, callback))
    }

    private inner class InviteMemberListener(val email: String, val callback: (Data) -> Unit)
        : LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            refreshMembers()
            callback.invoke(Data(true, response.message))
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            callback.invoke(Data(false, message))
        }
    }

    fun blockMember(member: Member, callback: (Data) -> Unit) {
        apiService.blockMember(session.getAppId(), member._id)
            .enqueue(BlockMemberListener(member, callback))
    }

    private inner class BlockMemberListener(val member: Member, val callback: (Data) -> Unit): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            refreshMembers()
            callback.invoke(Data(true, response.message))
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            callback.invoke(Data(false, message))
        }
    }

    private fun checkForTokenError(details: List<Error>) {
        details.forEach {
            if (it.code == AuthConstants.TOKEN_ERROR) {
                _authError.postValue(true)
            }
        }
    }

    data class Data(var success: Boolean = false,
                    var message: String? = null)
}