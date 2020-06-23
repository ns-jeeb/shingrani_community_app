package com.locked.shingranicommunity.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.locked.models.AnnouncementItem
import com.locked.shingranicommunity.locked.models.AnnouncementStatus
import com.locked.shingranicommunity.repositories.AnnouncementRepository
import com.locked.shingranicommunity.session.Session
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AnnouncementListViewModel @Inject constructor(
    private val repository: AnnouncementRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val res: ResourceProvider
) : ViewModel() {

    private val data: Data = Data()
    private val itemViewModelList: MutableMap<AnnouncementItem, ItemViewModel> = mutableMapOf()
    val message: LiveData<String> = data.message
    val list: LiveData<MutableList<AnnouncementItem>> = data.list

    private val fetchAnnouncementsObserver: Observer<in AnnouncementRepository.Data> = Observer {
        it?.let {
            if (it.success) {
                // data loaded
                data.list.postValue(repository.announcements)
            } else {
                data.message.postValue(it.message)
            }
        }
    }

    init {
        repository.fetchAnnouncements.observeForever(fetchAnnouncementsObserver)
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
        repository.fetchAnnouncements()
    }

    fun messageHandled() {
        data.message.value = null
    }

    override fun onCleared() {
        repository.fetchAnnouncements.removeObserver(fetchAnnouncementsObserver)
        clearItemViewModels()
        super.onCleared()
    }

    private fun clearItemViewModels() {
        for (entry in itemViewModelList) {
            entry.value.onCleared()
        }
    }

    fun getItemViewModel(position: Int): ItemViewModel? {
        val announceItem: AnnouncementItem? = list.value!!.getOrNull(position)
        announceItem?.let {
            var itemViewModel: ItemViewModel? = itemViewModelList.get(announceItem)
            if (itemViewModel == null) {
                itemViewModel = ItemViewModel(announceItem, session, navigation, repository)
                itemViewModelList.put(announceItem, itemViewModel)
            }
            return itemViewModel
        }
        return null
    }

    private fun announcementDeleted(announceItem: AnnouncementItem) {
        // update list and notify listeners
        data.list.postValue(repository.announcements)
    }

    private data class Data(
        val message: MutableLiveData<String> = MutableLiveData<String>()) {
        val list: MutableLiveData<MutableList<AnnouncementItem>> = MutableLiveData(mutableListOf())
    }

    inner class ItemViewModel(
        private val announceItem: AnnouncementItem,
        private val session: Session,
        private val navigation: Navigation,
        private val repository: AnnouncementRepository) {

        val deleteConfirmationTitle = res.getString(R.string.delete_announcement_confirm_title)
        val deleteConfirmationDesc = res.getString(R.string.delete_announcement_confirm_desc).format(announceItem.title)

        val data: ItemData = ItemData()
        val title: LiveData<String> = data.title
        val detail: LiveData<String> = data.detail
        val date: LiveData<String> = data.date
        val showDeleteConfirmation: LiveData<Boolean> = data.showDeleteConfirmation

        init {
            announceItem.observeStatus(this::onStatusChanged)
            // init title
            val titleStr = announceItem.title ?: ""
            data.title.value = titleStr
            // init detail
            val detailStr = announceItem.text ?: ""
            data.detail.value = detailStr
            // init date
            initDateTime()
        }

        private fun initDateTime() {
            val timeStr = announceItem.timestamp ?: ""
            val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())
            val toFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            try {
                val dt: Date = fromFormat.parse(timeStr)!!
                data.date.value = toFormat.format(dt)
            } catch (e: ParseException) {
                data.date.value = "-"
            }
        }

        fun delete(confirmed: Boolean = false) {
            if (confirmed) {
                data.showDeleteConfirmation.value = false
                repository.deleteAnnouncement(announceItem)
            } else {
                data.showDeleteConfirmation.postValue(true)
            }
        }

        private fun onStatusChanged(oldStatus: String?, newStatus: String?) {
            newStatus?.let {
                when(newStatus) {
                    AnnouncementStatus.DELETED.toString() -> this@AnnouncementListViewModel.announcementDeleted(announceItem)
                }
            }
        }

        fun onCleared() {
            announceItem.deObserveStatus(this::onStatusChanged)
        }
    }

    data class ItemData(
        val title: MutableLiveData<String> = MutableLiveData(""),
        val detail: MutableLiveData<String> = MutableLiveData(""),
        val date: MutableLiveData<String> = MutableLiveData("-"),
        val showDeleteConfirmation: MutableLiveData<Boolean> = MutableLiveData(false)
    )
}
