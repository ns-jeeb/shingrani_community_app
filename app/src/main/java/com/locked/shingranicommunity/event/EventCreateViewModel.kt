package com.locked.shingranicommunity.event

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.EventStatus
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class EventCreateViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session,
    private val navigation: Navigation)
    : ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData()
    val location: MutableLiveData<String> = MutableLiveData()
    val time: MutableLiveData<String> = MutableLiveData()
    val date: MutableLiveData<String> = MutableLiveData()
    val type: MutableLiveData<String> = MutableLiveData()
    val desc: MutableLiveData<String> = MutableLiveData()

    val data: Data = Data()
    val message: LiveData<String> = data.message

    fun create() {
        if (!session.isUserAdmin()) {
            data.message.postValue("You must be an admin to perform this operation!")
            return
        }
        val event = EventItem()
        event.name = title.value
        event.address = location.value
        event.time = getDateTime()
        event.type = type.value
        event.detail = desc.value
        event.app = session.getAppId()
        event.template = session.getEventTemplateId()
        event.observeStatus(this::onStatusChanged)
        repository.createEvent(event)
    }

    private fun onStatusChanged(old: String?, new: String?) {
        when (new) {
            EventStatus.CREATED.toString() -> {
                data.message.postValue("Event Created")
                navigation.createFinished()
            }
            EventStatus.CREATE_FAILED.toString() -> {
                data.message.postValue("Failed to Create Event")
            }
        }
    }

    private fun getDateTime(): String {
        // init date & time
//        val toFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//        val dt: Date = toFormat.parse(date.value)
//        val toDateFormat = SimpleDateFormat("E, MMM dd", Locale.getDefault())
//        val toTimeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
//        date.value = toDateFormat.format(dt)
//        time.value = toTimeFormat.format(dt)
        return date.value + "T" + time.value
    }
    fun navigateToSearchAddress(context: Context) {
        navigation.navigateAutoComplete(true)
    }

}

data class Data(val message: MutableLiveData<String> = MutableLiveData())
