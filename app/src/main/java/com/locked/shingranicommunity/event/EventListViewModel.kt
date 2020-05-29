package com.locked.shingranicommunity.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.EventStatus
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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

    init {
        repository.fetchEvents.observeForever(fetchEventsObserver)
    }

    fun load() {
        repository.fetchEvents()
    }

    fun messageHandled() {
        data.message.value = null
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

        val deleteConfirmationTitle = resourceProvider.getString(R.string.delete_event_confirm_title)
        val deleteConfirmationDesc = resourceProvider.getString(R.string.delete_event_confirm_desc).format(eventItem.name)

        val data: ItemData = ItemData()
        val name: LiveData<String> = data.name
        val desc: LiveData<String> = data.desc
        val type: LiveData<String> = data.type
        val date: LiveData<String> = data.date
        val time: LiveData<String> = data.time
        val attendees: LiveData<Int> = data.attendees
        val showAccept: LiveData<Boolean> = data.showAccept
        val showReject: LiveData<Boolean> = data.showReject
        val showDelete: LiveData<Boolean> = data.showDelete
        val showShare: LiveData<Boolean> = data.showShare
        val showMap: LiveData<Boolean> = data.showMap
        val showDeleteConfirmation: LiveData<Boolean> = data.showDeleteConfirmation

        init {
            eventItem.observeStatus(this::onStatusChanged)
            // init name
            val nameStr = eventItem.name ?: ""
            data.name.value = nameStr
            // init desc
            val descStr = eventItem.detail ?: ""
            data.desc.value = descStr
            // init type
            val typeStr = eventItem.type ?: "Event"
            data.type.value = typeStr
            // init date & time
            initDateTime()
            // init accept/reject buttons
            initAcceptReject()
            // init delete
            data.showDelete.value = session.isUserAdmin()
        }

        private fun initDateTime() {
            val timeStr = eventItem.time ?: ""
            val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val toDateFormat = SimpleDateFormat("E, MMM dd", Locale.getDefault())
            val toTimeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
            try {
                val dt: Date = fromFormat.parse(timeStr)
                data.date.value = toDateFormat.format(dt)
                data.time.value = toTimeFormat.format(dt)
            } catch (e: ParseException) {
                data.date.value = "-"
                data.time.value = "-"
            }
        }

        private fun initAcceptReject() {
            val acceptedStr = eventItem.accepted?.trim() ?: ""
            val rejectedStr = eventItem.rejected?.trim() ?: ""
            data.showAccept.postValue(!acceptedStr.contains(session.getUserId()!!))
            data.showReject.postValue(!rejectedStr.contains(session.getUserId()!!))
            data.attendees.postValue(if (acceptedStr.isNotBlank()) { acceptedStr.split(",").size } else { 0 })
        }

        fun accept() {
            repository.accept(eventItem)
        }

        fun reject() {
            repository.reject(eventItem)
        }

        fun openMap() {
            eventItem.address?.let {
                navigation.navigateToMap(it)
            }
        }

        fun share() {
            // todo
        }

        fun delete(confirmed: Boolean = false) {
            if (confirmed) {
                data.showDeleteConfirmation.value = false
                repository.deleteEvent(eventItem)
            } else {
                data.showDeleteConfirmation.postValue(true)
            }
        }

        private fun onStatusChanged(oldStatus: String?, newStatus: String?) {
            newStatus?.let {
                when(newStatus) {
                    EventStatus.DELETED.toString() -> this@EventListViewModel.eventDeleted(eventItem)
                    EventStatus.ACCEPTED.toString() -> initAcceptReject()
                    EventStatus.REJECTED.toString() -> initAcceptReject()
                }
            }
        }

        fun onCleared() {
            eventItem.deObserveStatus(this::onStatusChanged)
        }
    }

    data class ItemData(
        val name: MutableLiveData<String> = MutableLiveData(""),
        val desc: MutableLiveData<String> = MutableLiveData(""),
        val type: MutableLiveData<String> = MutableLiveData("Event"),
        val date: MutableLiveData<String> = MutableLiveData(""),
        val time: MutableLiveData<String> = MutableLiveData(""),
        val attendees: MutableLiveData<Int> = MutableLiveData(0),
        val showAccept: MutableLiveData<Boolean> = MutableLiveData(true),
        val showReject: MutableLiveData<Boolean> = MutableLiveData(true),
        val showDelete: MutableLiveData<Boolean> = MutableLiveData(true),
        val showShare: MutableLiveData<Boolean> = MutableLiveData(true),
        val showMap: MutableLiveData<Boolean> = MutableLiveData(true),
        val showDeleteConfirmation: MutableLiveData<Boolean> = MutableLiveData(false)
    )
}