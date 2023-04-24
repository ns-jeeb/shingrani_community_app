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
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

@AppScope
class MemberRepository @Inject constructor(
    val apiService: LockedApiService,
    val session: Session) {

    private var loading: Boolean = false
    private val _authError: MutableLiveData<Boolean> = MutableLiveData(false)
    val authError: LiveData<Boolean> = _authError
    var members: MutableLiveData<MutableList<Member>> = MutableLiveData(mutableListOf())

    init {
        session.loginState.observeForever(Observer {
            _authError.value = !it
            if (it) {
                refreshMembers()
            }
        })
        session.appState.observeForever(Observer {
            if (it) {
                refreshMembers()
            }
        })
    }

    fun fetchMembers() {
        refreshMembers()
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
                response.add(0, Member("-1", session.getAppId(), it.username, MemberState.JOINED.state, "(416) 554 - 4444",it))
            }
            val loadedMembers: MutableList<Member> = response.distinctBy { it.email }.toMutableList()
            loadedMembers.forEach { member ->
                member.user?.let {
                    member.isAdmin = session.isAdmin(it)
                    member.isMe = session.isMe(it)
                    // TODO:  his need to be removed once backend updated with phonenumber field
                    member.phoneNumber = "(416) 552 -5656"
                }
            }
            members.postValue(loadedMembers)
            loading = false
        }
        override fun fail(message: String, details: List<Error>) {
            loading = false
            checkForTokenError(details)
        }
    }

    fun fetchMember(memberId: String, callback: ((Member) -> Unit)) {
        val foundMember = members.value?.find { it._id == memberId }
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

    fun unblockMember(member: Member, callback: (Data) -> Unit) {
        apiService.unblockMember(session.getAppId(), member._id)
            .enqueue(UnblockMemberListener(member, callback))
    }

    private inner class UnblockMemberListener(val member: Member, val callback: (Data) -> Unit): LockedCallback<LockResponse>() {
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