package com.locked.shingranicommunity.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.locked.models.Rsvp
import com.locked.shingranicommunity.locked.models.RsvpObject
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.Item
import com.locked.shingranicommunity.models.User
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val data: Data = Data()
    val message: LiveData<String> = data.message
    val list: LiveData<MutableList<EventItem>> = data.list

    private val fetchEventsObserver: Observer<in EventRepository.Data> = Observer {
        it?.let {
            if (it.success) {
                // data loaded
                data.list.postValue(repository.events)
            } else {
                data.message.postValue(it.message)
            }
        }
    }

    fun isAdminUser(): Boolean{return session.isUserAdmin()}
    fun currentUser(): User?{return session.getUser()}
    init {
        repository.fetchEvents.observeForever(fetchEventsObserver)
    }

    fun load() {
        repository.fetchEvents()
    }

    override fun onCleared() {
        repository.fetchEvents.removeObserver(fetchEventsObserver)
        super.onCleared()
    }

//    fun accepted(item: Item): MutableLiveData<String>? {
//        val rsvp: RsvpObject? =
//                RsvpObject(
//                        Rsvp(
//                                "Accepted",
//                                session.getUserId()!!
//                        )
//                )
//        return repository.accepted(rsvp!!,item._id)
//    }
//
//    fun rejected(item: Item): MutableLiveData<String>? {
//        var rsvp: RsvpObject? =
//                RsvpObject(
//                        Rsvp(
//                                "Rejected",
//                                session.getUserId()!!
//                        )
//                )
//        return itemEventHandler.rejected(rsvp!!,item._id)
//    }

    private data class Data(
        val message: MutableLiveData<String> = MutableLiveData<String>()) {
        val list: MutableLiveData<MutableList<EventItem>> = MutableLiveData(mutableListOf())
    }
}