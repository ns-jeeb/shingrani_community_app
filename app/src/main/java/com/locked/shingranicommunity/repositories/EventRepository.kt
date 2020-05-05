package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.di2.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.Error
import com.locked.shingranicommunity.locked.models.LockResponse
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.Status
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
    var events: MutableList<EventItem> = mutableListOf()

    init {
        refreshEvents()
    }

    fun fetchEvents() {
       if (events.isEmpty()) {
           refreshEvents()
       } else {
           _fetchEvents.postValue(Data(true))
       }
    }

    fun refreshEvents() {
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
        apiService.createEvent(event)
            .enqueue(CreateEventListener(event))
    }

    fun deleteEvent(event: EventItem) {
        apiService.deleteEvent(event._id!!)
            .enqueue(DeleteEventListener(event))
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
            _fetchEvents.postValue(Data(true))
        }
    }

    @Suppress("UNREACHABLE_CODE")
    private inner class CreateEventListener(val event: EventItem): LockedCallback<EventItem>() {
        override fun success(response: EventItem) {
            events.add(event)
            event.status = Status.CREATED
        }
        override fun fail(message: String, details: List<Error>) {
            event.status = Status.CREATE_FAILED
        }
    }

    @Suppress("UNREACHABLE_CODE")
    private inner class DeleteEventListener(val event: EventItem): LockedCallback<LockResponse>() {
        override fun success(response: LockResponse) {
            events.remove(event)
            event.status = Status.DELETED
        }
        override fun fail(message: String, details: List<Error>) {
            event.status = Status.DELETE_FAILED
        }
    }

    data class Data(var success: Boolean = false,
                    var message: String? = null)
}