package com.locked.shingranicommunity.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.common.BaseActivity
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.dashboard2.DashboardActivity
import com.locked.shingranicommunity.locked.models.Rsvp
import com.locked.shingranicommunity.locked.models.RsvpObject
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.Item
import com.locked.shingranicommunity.models.Status
import com.locked.shingranicommunity.models.User
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class EventListViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val data: Data = Data()
    private val itemViewModelList: MutableMap<EventItem, ItemViewModel> = mutableMapOf()
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
        clearItemViewModels()
        super.onCleared()
    }

    private fun clearItemViewModels() {
        for (entry in itemViewModelList) {
            entry.value.onCleared()
        }
    }

    // use this onBind() of List Adapter
    fun getItemViewModel(itemIndex: Int): ItemViewModel? {
        val eventItem: EventItem? = list.value!!.getOrNull(itemIndex)
        eventItem?.let {
            var itemViewModel: ItemViewModel? = itemViewModelList.get(eventItem)
            if (itemViewModel == null) {
                itemViewModel = ItemViewModel(eventItem, session, navigation, repository)
                itemViewModelList.put(eventItem, itemViewModel)
            }
            return itemViewModel
        }
        return null
    }

    private fun eventDeleted(eventItem: EventItem) {
        // update list and notify listeners
        data.list.postValue(repository.events)
    }

    private data class Data(
        val message: MutableLiveData<String> = MutableLiveData<String>()) {
        val list: MutableLiveData<MutableList<EventItem>> = MutableLiveData(mutableListOf())
    }

    inner class ItemViewModel(
        private val eventItem: EventItem,
        private val session: Session,
        private val navigation: Navigation,
        private val repository: EventRepository) {

        val name: LiveData<String>
        val desc: LiveData<String>
        val date: LiveData<String>
        val time: LiveData<String>
        val showAccept: LiveData<Boolean>
        val showReject: LiveData<Boolean>
        val showDelete: LiveData<Boolean>
        val showShare: LiveData<Boolean>
        val showMap: LiveData<Boolean>

        init {
            // init name
            val nameStr = eventItem.name ?: ""
            name = MutableLiveData(nameStr)
            // init desc
            val descStr = eventItem.detail ?: ""
            desc = MutableLiveData(descStr)
            // init date & time
            val timeStr = eventItem.time ?: ""
            val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dt: Date = fromFormat.parse(timeStr)
            val toDateFormat = SimpleDateFormat("E, MMM dd", Locale.getDefault())
            val toTimeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
            date = MutableLiveData(toDateFormat.format(dt))
            time = MutableLiveData(toTimeFormat.format(dt))
            // init accept/reject buttons
            val acceptedStr = eventItem.accepted ?: ""
            val rejectedStr = eventItem.rejected ?: ""
            showAccept = MutableLiveData(!acceptedStr.contains(session.getUserId()!!))
            showReject = MutableLiveData(!rejectedStr.contains(session.getUserId()!!))
            // init delete
            showDelete = MutableLiveData(session.isUserAdmin())
            // init share
            showShare = MutableLiveData(true)
            // init map
            showMap = MutableLiveData(true)
        }

        fun accept() {
            val rsvp: RsvpObject? =
                    RsvpObject(
                            Rsvp(
                                    "Accepted",
                                    session.getUserId()!!
                            )
                    )
            repository.acceptedAttending(rsvp!!,eventItem._id)
        }

        fun reject() {
            var rsvp: RsvpObject? =
                    RsvpObject(
                            Rsvp(
                                    "Rejected",
                                    session.getUserId()!!
                            )
                    )
            repository.rejectedAttending(rsvp!!,eventItem._id)
        }

        fun openMap(address: String) {
            navigation.navigateToNext(address)
        }

        fun share() {
            // todo
        }

        fun delete() {
            eventItem.observeStatus(this::onStatusChanged)
            repository.deleteEvent(eventItem)
        }

        private fun onStatusChanged(oldStatus: String?, newStatus: String?) {
            newStatus?.let {
                if (newStatus == Status.DELETED) {
                    this@EventListViewModel.eventDeleted(eventItem)
                }
            }
        }

        fun onCleared() {
            eventItem.deObserveStatus(this::onStatusChanged)
        }
    }
}