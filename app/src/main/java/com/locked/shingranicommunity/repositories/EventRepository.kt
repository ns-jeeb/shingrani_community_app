package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.auth.AuthConstants
import com.locked.shingranicommunity.di2.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.*
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.EventStatus
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

@AppScope
class EventRepository @Inject constructor(
    val apiService: LockedApiService,
    val session: Session
) {

    private val _fetchEvents: MutableLiveData<Data> = MutableLiveData<Data>()
    private var loading: Boolean = false
    val fetchEvents: LiveData<Data> = _fetchEvents
    val _authError: MutableLiveData<Boolean> = MutableLiveData(false)
    val authError: LiveData<Boolean> = _authError
    var events: MutableList<EventItem> = mutableListOf()

    init {
        session.loginState.observeForever(Observer { _authError.value = !it })
        refreshEvents()
    }

    fun fetchEvents() {
       if (events.isEmpty()) {
           refreshEvents()
       } else {
           _fetchEvents.postValue(Data(true))
       }
    }

    private fun refreshEvents() {
        if (!loading) {
            loading = true
            apiService.getEventList(session.getAppId(), session.getEventTemplateId())
                .enqueue(FetchEventsListener())
        }
    }

    fun fetchEvent(eventId: String, callback: ((EventItem) -> Unit)) {
        val foundEvent = events.find { it._id == eventId }
        if (foundEvent != null) {
            callback.invoke(foundEvent)
        }
    }

    fun createEvent(event: EventItem) {
        if (event.accepted == null) { event.accepted = "" }
        if (event.rejected == null) { event.rejected = "" }
        apiService.createEvent(event)
            .enqueue(CreateEventListener(event))
    }

    fun deleteEvent(event: EventItem) {
        apiService.deleteEvent(event._id!!)
            .enqueue(DeleteEventListener(event))
    }

    fun accept(event: EventItem) {
        val rsvp = RsvpObject(Rsvp("Accepted", session.getUserId()!!))
        apiService.accept(event._id!!, rsvp).enqueue(AcceptedListener(event))
    }

    fun reject(event: EventItem) {
        val rsvp = RsvpObject(Rsvp("Rejected", session.getUserId()!!))
        apiService.accept(event._id!!, rsvp).enqueue(RejectedListener(event))
    }

    private fun checkForTokenError(details: List<Error>) {
        details.forEach {
            if (it.code == AuthConstants.TOKEN_ERROR) {
                _authError.postValue(true)
            }
        }
    }

    @Suppress("UNREACHABLE_CODE")
    private inner class FetchEventsListener(): LockedCallback<MutableList<EventItem>>() {
        override fun success(response: MutableList<EventItem>) {
            events.clear()
            events.addAll(response)
            loading = false
            _fetchEvents.postValue(Data(true))
        }
        override fun fail(message: String, details: List<Error>) {
            loading = false
            checkForTokenError(details)
            _fetchEvents.postValue(Data(false))
        }
    }

    private inner class CreateEventListener(val event: EventItem): LockedCallback<CreateResponse<EventItem>>() {
        override fun success(response: CreateResponse<EventItem>) {
            response.item?.let { event.update(it) }
            events.add(event)
            event.status = EventStatus.CREATED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            event.status = EventStatus.CREATE_FAILED.toString()
        }
    }

    private inner class DeleteEventListener(val event: EventItem): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            events.remove(event)
            event.status = EventStatus.DELETED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            event.status = EventStatus.DELETE_FAILED.toString()
        }
    }

    private inner class AcceptedListener(val event: EventItem): LockedCallback<MutableLiveData<RsvpObject>>() {
        override fun success(response: MutableLiveData<RsvpObject>) {
            // update the accepted list
            if (event.accepted == null) event.accepted = ""
            if (event.rejected == null) event.rejected = ""
            if (!event.accepted!!.contains(session.getUserId()!!)) {
                event.accepted = event.accepted!!
                    .split(",")
                    .map { it.trim() }
                    .plus(session.getUserId())
                    .joinToString(separator = ",")
            }
            // update the rejected list
            if (event.rejected!!.contains(session.getUserId()!!)) {
                event.rejected = event.rejected!!
                    .split(",")
                    .map { it.trim() }
                    .minus(session.getUserId())
                    .joinToString(separator = ",")
            }
            // update status
            event.status = EventStatus.ACCEPTED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            event.status = EventStatus.ACCEPT_FAILED.toString()
        }
    }

    private inner class RejectedListener(val event: EventItem): LockedCallback<MutableLiveData<RsvpObject>>() {
        override fun success(response: MutableLiveData<RsvpObject>) {
            // update the accepted list
            if (event.accepted == null) event.accepted = ""
            if (event.rejected == null) event.rejected = ""
            if (!event.rejected!!.contains(session.getUserId()!!)) {
                event.rejected = event.rejected!!
                    .split(",")
                    .map { it.trim() }
                    .plus(session.getUserId())
                    .joinToString(separator = ",")
            }
            // update the rejected list
            if (event.accepted!!.contains(session.getUserId()!!)) {
                event.accepted = event.accepted!!
                    .split(",")
                    .map { it.trim() }
                    .minus(session.getUserId())
                    .joinToString(separator = ",")
            }
            // update status
            event.status = EventStatus.REJECTED.toString()
        }
        override fun fail(message: String, details: List<Error>) {
            checkForTokenError(details)
            event.status = EventStatus.REJECT_FAILED.toString()
        }
    }

    data class Data(var success: Boolean = false,
                    var message: String? = null)
}