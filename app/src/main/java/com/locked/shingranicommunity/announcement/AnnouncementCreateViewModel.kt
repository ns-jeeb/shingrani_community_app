package com.locked.shingranicommunity.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.models.AnnouncementItem
import com.locked.shingranicommunity.models.AnnouncementStatus
import com.locked.shingranicommunity.repositories.AnnouncementRepository
import com.locked.shingranicommunity.session.Session
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AnnouncementCreateViewModel  @Inject constructor(
    private val repository: AnnouncementRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val res: ResourceProvider) : ViewModel() {

    private val data = Data()
    private val validation = DataValidation()
    var isFormValid: Boolean = validation.isValid()

    val pageTitle: String = res.getString(R.string.event_create_page_title)
    val title: LiveData<String> = data.title
    val text: LiveData<String> = data.text
    val message: LiveData<String> = data.message

    val isTitleValid: LiveData<String> = validation.isTitleValid
    val isTextValid: LiveData<String> = validation.isTextValid

    private val createFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())

    fun setTitle(title: String) {
        data.title.postValue(title)
    }

    fun setText(text: String) {
        data.text.postValue(text)
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
            val announcement = AnnouncementItem()
            announcement.title = title.value
            announcement.text = text.value
            announcement.timestamp = getTimeStamp()
            announcement.app = session.getAppId()
            announcement.template = session.getAnnouncementTemplateId()
            announcement.observeStatus(this::onStatusChanged)
            repository.createAnnouncement(announcement)
        }
    }

    private fun getTimeStamp(): String {
        val timeStampDate: Date = Calendar.getInstance().time
        return createFormat.format(timeStampDate)
    }

    private fun validateForm() {
        if (title.value.isNullOrBlank() || title.value!!.length < 5) {
            validation.isTitleValid.value = res.getString(R.string.validation_title)
        } else {
            validation.isTitleValid.value = null
        }
        if (text.value.isNullOrBlank() || text.value!!.length < 10) {
            validation.isTextValid.value = res.getString(R.string.validation_desc)
        } else {
            validation.isTextValid.value = null
        }
        isFormValid = validation.isValid()
    }

    private fun onStatusChanged(old: String?, new: String?) {
        when (new) {
            AnnouncementStatus.CREATED.toString() -> {
                data.message.postValue(res.getString(R.string.announcement_created))
                navigation.createFinished()
            }
            AnnouncementStatus.CREATE_FAILED.toString() -> {
                data.message.postValue(res.getString(R.string.announcement_create_failed))
            }
        }
    }
}


data class Data(
    val title: MutableLiveData<String> = MutableLiveData(),
    val text: MutableLiveData<String> = MutableLiveData(),
    val message: MutableLiveData<String> = MutableLiveData()
)

data class DataValidation(
    val isTitleValid: MutableLiveData<String> = MutableLiveData(),
    val isTextValid: MutableLiveData<String> = MutableLiveData()
) {

    fun isValid(): Boolean {
        return isTitleValid.value == null
                && isTextValid.value == null
    }
}

