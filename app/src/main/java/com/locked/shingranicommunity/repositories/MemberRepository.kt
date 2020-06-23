package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.auth.AuthConstants
import com.locked.shingranicommunity.di.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.*
import com.locked.shingranicommunity.locked.models.AnnouncementItem
import com.locked.shingranicommunity.locked.models.AnnouncementStatus
import com.locked.shingranicommunity.locked.models.response.CreateResponse
import com.locked.shingranicommunity.locked.models.response.LockResponse
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

@AppScope
class MemberRepository @Inject constructor(
    val apiService: LockedApiService,
    val session: Session) {

    private var loading: Boolean = false
    private val _fetchMembers: MutableLiveData<Data> = MutableLiveData<Data>()
    private val _authError: MutableLiveData<Boolean> = MutableLiveData(false)
    val fetchMembers: LiveData<Data> = _fetchMembers
    val authError: LiveData<Boolean> = _authError
    var members: MutableList<AnnouncementItem> = mutableListOf()

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
            apiService.getAnnouncementList(session.getAppId(), session.getAnnouncementTemplateId()) // todo
                .enqueue(FetchMembersListener())
        }
    }

    fun fetchMember(memberId: String, callback: ((AnnouncementItem) -> Unit)) { // todo
        val foundMember = members.find { it._id == memberId }
        if (foundMember != null) {
            callback.invoke(foundMember)
        }
    }

    fun inviteMember(member: AnnouncementItem) {// todo
        apiService.createAnnouncement(member)
            .enqueue(CreateAnnouncementListener(member))
    }

    fun removeMember(member: AnnouncementItem) {// todo
        apiService.deleteAnnouncement(member._id!!)
            .enqueue(DeleteAnnouncementListener(member))
    }

    private fun checkForTokenError(details: List<Error>) {
        details.forEach {
            if (it.code == AuthConstants.TOKEN_ERROR) {
                _authError.postValue(true)
            }
        }
    }

    private inner class FetchMembersListener(): LockedCallback<MutableList<AnnouncementItem>>() {
        override fun success(response: MutableList<AnnouncementItem>) {
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

    private inner class CreateAnnouncementListener(val announcement: AnnouncementItem): LockedCallback<CreateResponse<AnnouncementItem>>() {
        override fun success(response: CreateResponse<AnnouncementItem>) {
            response.item?.let { announcement.update(it) }
            members.add(announcement)
            announcement.status = AnnouncementStatus.CREATED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            announcement.status = AnnouncementStatus.CREATE_FAILED.toString()
        }
    }

    private inner class DeleteAnnouncementListener(val announcement: AnnouncementItem): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            members.remove(announcement)
            announcement.status = AnnouncementStatus.DELETED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            announcement.status = AnnouncementStatus.DELETE_FAILED.toString()
        }
    }

    data class Data(var success: Boolean = false,
                    var message: String? = null)
}