package com.locked.shingranicommunity.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventCreateViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session)
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
        repository.createEvent(event)
    }

    private fun getDateTime(): String {
        // init date & time
//        val toFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//        val dt: Date = fromFormat.parse(timeStr)
//        val toDateFormat = SimpleDateFormat("E, MMM dd", Locale.getDefault())
//        val toTimeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
//        data.date.value = toDateFormat.format(dt)
//        data.time.value = toTimeFormat.format(dt)
        return date.value + "'T'" + time.value
    }
}

data class Data(val message: MutableLiveData<String> = MutableLiveData())
