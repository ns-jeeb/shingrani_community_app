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
class AnnouncementRepository @Inject constructor(
    val apiService: LockedApiService,
    val session: Session
) {

    private var loading: Boolean = false
    private val _fetchAnnouncements: MutableLiveData<Data> = MutableLiveData<Data>()
    private val _authError: MutableLiveData<Boolean> = MutableLiveData(false)
    val fetchAnnouncements: LiveData<Data> = _fetchAnnouncements
    val authError: LiveData<Boolean> = _authError
    var announcements: MutableList<AnnouncementItem> = mutableListOf()

    init {
        session.loginState.observeForever(Observer { _authError.value = !it })
        refreshAnnouncements()
    }

    fun fetchAnnouncements() {
       if (announcements.isEmpty()) {
           refreshAnnouncements()
       } else {
           _fetchAnnouncements.postValue(Data(true))
       }
    }

    private fun refreshAnnouncements() {
        if (!loading) {
            loading = true
            apiService.getAnnouncementList(session.getAppId(), session.getAnnouncementTemplateId())
                .enqueue(FetchAnnouncementsListener())
        }
    }

    fun fetchAnnouncement(announcementId: String, callback: ((AnnouncementItem) -> Unit)) {
        val foundAnnouncement = announcements.find { it._id == announcementId }
        if (foundAnnouncement != null) {
            callback.invoke(foundAnnouncement)
        }
    }

    fun createAnnouncement(event: AnnouncementItem) {
        apiService.createAnnouncement(event)
            .enqueue(CreateAnnouncementListener(event))
    }

    fun deleteAnnouncement(event: AnnouncementItem) {
        apiService.deleteAnnouncement(event._id!!)
            .enqueue(DeleteAnnouncementListener(event))
    }

    private fun checkForTokenError(details: List<Error>) {
        details.forEach {
            if (it.code == AuthConstants.TOKEN_ERROR) {
                _authError.postValue(true)
            }
        }
    }

    private inner class FetchAnnouncementsListener(): LockedCallback<MutableList<AnnouncementItem>>() {
        override fun success(response: MutableList<AnnouncementItem>) {
            announcements.clear()
            announcements.addAll(response)
            loading = false
            _fetchAnnouncements.postValue(Data(true))
        }
        override fun fail(message: String, details: List<Error>) {
            loading = false
            checkForTokenError(details)
            _fetchAnnouncements.postValue(Data(false))
        }
    }

    private inner class CreateAnnouncementListener(val announcement: AnnouncementItem): LockedCallback<CreateResponse<AnnouncementItem>>() {
        override fun success(response: CreateResponse<AnnouncementItem>) {
            response.item?.let { announcement.update(it) }
            announcements.add(announcement)
            announcement.status = AnnouncementStatus.CREATED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            announcement.status = AnnouncementStatus.CREATE_FAILED.toString()
        }
    }

    private inner class DeleteAnnouncementListener(val announcement: AnnouncementItem): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            announcements.remove(announcement)
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