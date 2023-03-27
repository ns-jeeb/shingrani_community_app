package com.locked.shingranicommunity.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.DrawableLoader
import com.locked.shingranicommunity.common.ImageLoader
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.locked.models.EventItem
import com.locked.shingranicommunity.locked.models.EventStatus
import com.locked.shingranicommunity.locked.models.EventType
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.List

class EventListViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val res: ResourceProvider
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
        repository.authError.observeForever(Observer { authErrorOccurred(it) })
    }

    private fun authErrorOccurred(it: Boolean?) {
        it?.let {
            if (it) {
                navigation.navigateToLogin(true)
            }
        }
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
        val eventItem: EventItem? = list.value!!.sortedByDescending { it.time }.getOrNull(itemIndex)
        eventItem?.let {
            var itemViewModel: ItemViewModel? = itemViewModelList[eventItem]
            if (itemViewModel == null) {
                itemViewModel = ItemViewModel(eventItem, session, navigation, repository)
                itemViewModelList[eventItem] = itemViewModel
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

        val deleteConfirmationTitle = res.getString(R.string.delete_event_confirm_title)
        val deleteConfirmationDesc = res.getString(R.string.delete_event_confirm_desc).format(eventItem.name)

        val data: ItemData = ItemData()
        var backgroundImage: ImageLoader = DrawableLoader(R.drawable.img_event_1)
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
            eventItem.address?.let {data.showMap.value = true}
            // init background image
            initBackground()
        }

        private fun initBackground() {
            when (eventItem.type) {
                EventType.EVENT.type -> {
                    backgroundImage = when((1..2).random()) {
                        2 -> DrawableLoader(R.drawable.img_event_2)
                        else -> DrawableLoader(R.drawable.img_event_1)
                    }
                }
                EventType.BIRTHDAY.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_birthday_1)
                }
                EventType.FUNDRAISING.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_fundraising_1)
                }
                EventType.FUNERAL.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_funeral_1)
                }
                EventType.GATHERING.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_gathering_1)
                }
                EventType.MEETING.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_meeting_1)
                }
                EventType.PARTY.type -> {
                    backgroundImage = when((1..2).random()) {
                        2 -> DrawableLoader(R.drawable.img_party_2)
                        else -> DrawableLoader(R.drawable.img_party_1)
                    }
                }
                EventType.WEDDING.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_wedding_1)
                }
                EventType.ENGAGEMENT.type -> {
                    backgroundImage = DrawableLoader(R.drawable.img_engagement_1)
                }
                EventType.OUTING.type -> {
                    backgroundImage = when((1..5).random()) {
                        2 -> DrawableLoader(R.drawable.img_outing_2)
                        3 -> DrawableLoader(R.drawable.img_outing_3)
                        4 -> DrawableLoader(R.drawable.img_outing_4)
                        5 -> DrawableLoader(R.drawable.img_outing_5)
                        else -> DrawableLoader(R.drawable.img_outing_1)
                    }
                }
            }
        }

        private fun initDateTime() {
            val timeStr = eventItem.time ?: ""
            val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())
            val toDateFormat = SimpleDateFormat("E, MMM dd", Locale.getDefault())
            val toTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            try {
                val dt: Date = fromFormat.parse(timeStr)!!
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
            data.attendees.postValue(
                if (acceptedStr.isNotBlank()) {
                    acceptedStr.split(",").filter { it.isNotBlank() }.size
                } else { 0 }
            )
        }

        fun accept() {
            repository.accept(eventItem)
        }

        fun reject() {
            repository.reject(eventItem)
        }

        fun openMap() {
            navigation.navigateToMap(eventItem.address!!)
        }

        fun share() {
            navigation.navigateShare(eventItem)
        }

        fun delete(confirmed: Boolean = false) {
            if (confirmed) {
                data.showDeleteConfirmation.value = false
                repository.deleteEvent(eventItem)
            } else {
                data.showDeleteConfirmation.postValue(true)
            }
        }

        fun cancelDelete() {
            data.showDeleteConfirmation.value = false
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
        val showMap: MutableLiveData<Boolean> = MutableLiveData(false),
        val showDeleteConfirmation: MutableLiveData<Boolean> = MutableLiveData(false)
    )
}