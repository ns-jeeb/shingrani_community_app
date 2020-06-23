package com.locked.shingranicommunity.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.EventStatus
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventCreateViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val res: ResourceProvider)
    : ViewModel() {

    private val data = Data()
    private val validation = DataValidation()
    var isFormValid: Boolean = validation.isValid()
    val eventTypes: Array<String> = res.getStringArray(R.array.event_types)

    val pageTitle: String = res.getString(R.string.event_create_page_title)
    val title: LiveData<String> = data.title
    val location: LiveData<String> = data.location
    val time: LiveData<String> = data.time
    val date: LiveData<String> = data.date
    val type: LiveData<String> = data.type
    val desc: LiveData<String> = data.desc
    val message: LiveData<String> = data.message

    val isTitleValid: LiveData<String> = validation.isTitleValid
    val isLocationValid: LiveData<String> = validation.isLocationValid
    val isTimeValid: LiveData<String> = validation.isTimeValid
    val isDateValid: LiveData<String> = validation.isDateValid
    val isTypeValid: LiveData<String> = validation.isTypeValid
    val isDescriptionValid: LiveData<String> = validation.isDescriptionValid

    private val createFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

    private var hour: Int? = null
    private var minute: Int? = null
    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null

    init {
        data.type.value = eventTypes[0]
    }

    fun setTitle(title: String) {
        data.title.postValue(title)
    }

    fun setLocation(location: String) {
        data.location.postValue(location)
    }

    fun setTime(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
        val formatter = NumberFormat.getNumberInstance()
        formatter.maximumIntegerDigits = 2
        formatter.minimumIntegerDigits = 2
        data.time.postValue("${if(hour>12) hour-12 else hour}:${formatter.format(minute)} ${if (hour>11) "PM" else "AM"}")
    }

    fun setDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        data.date.postValue(dateFormat.format(cal.time))
    }

    fun setType(type: String) {
        data.type.postValue(type)
    }

    fun setDescription(desc: String) {
        data.desc.postValue(desc)
    }

    fun searchAddress() {
        navigation.navigateSearchAddress(true)
    }

    fun messageHandled() {
        data.message.postValue(null)
    }

    fun create() {
        if (!session.isUserAdmin()) {
            data.message.postValue(res.getString(R.string.must_be_admin))
            return
        }
        validateForm()
        if (isFormValid) {
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
    }
    
    private fun validateForm() {
        if (title.value.isNullOrBlank() || title.value!!.length < 5) {
            validation.isTitleValid.value = res.getString(R.string.validation_title)
        } else {
            validation.isTitleValid.value = null
        }
        if (location.value.isNullOrBlank() || location.value!!.length < 5) {
            validation.isLocationValid.value = res.getString(R.string.validation_location)
        } else {
            validation.isLocationValid.value = null
        }
        if (date.value.isNullOrBlank()) {
            validation.isDateValid.value = res.getString(R.string.validation_date)
        } else {
            validation.isDateValid.value = null
        }
        if (time.value.isNullOrBlank()) {
            validation.isTimeValid.value = res.getString(R.string.validation_time)
        } else {
            validation.isTimeValid.value = null
        }
        if (type.value.isNullOrBlank()) {
            validation.isTypeValid.value = res.getString(R.string.validation_type)
        } else {
            validation.isTypeValid.value = null
        }
        if (desc.value.isNullOrBlank() || desc.value!!.length < 10) {
            validation.isDescriptionValid.value = res.getString(R.string.validation_desc)
        } else {
            validation.isDescriptionValid.value = null
        }
        isFormValid = validation.isValid()
    }

    private fun onStatusChanged(old: String?, new: String?) {
        when (new) {
            EventStatus.CREATED.toString() -> {
                data.message.postValue(res.getString(R.string.event_created))
                navigation.createFinished()
            }
            EventStatus.CREATE_FAILED.toString() -> {
                data.message.postValue(res.getString(R.string.event_create_failed))
            }
        }
    }

    private fun getDateTime(): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year!!, month!!, day!!, hour!!, minute!!)
        return createFormat.format(calendar.time)
    }
}

data class Data(
    val title: MutableLiveData<String> = MutableLiveData(),
    val location: MutableLiveData<String> = MutableLiveData(),
    val time: MutableLiveData<String> = MutableLiveData(),
    val date: MutableLiveData<String> = MutableLiveData(),
    val type: MutableLiveData<String> = MutableLiveData(),
    val desc: MutableLiveData<String> = MutableLiveData(),
    val message: MutableLiveData<String> = MutableLiveData())

data class DataValidation(
    val isTitleValid: MutableLiveData<String> = MutableLiveData(),
    val isLocationValid: MutableLiveData<String> = MutableLiveData(),
    val isTimeValid: MutableLiveData<String> = MutableLiveData(),
    val isDateValid: MutableLiveData<String> = MutableLiveData(),
    val isTypeValid: MutableLiveData<String> = MutableLiveData(),
    val isDescriptionValid: MutableLiveData<String> = MutableLiveData()) {
    
    fun isValid(): Boolean {
        return isTitleValid.value == null
                && isLocationValid.value == null
                && isTimeValid.value == null
                && isDateValid.value == null
                && isTypeValid.value == null
                && isDescriptionValid.value == null
    }
}
