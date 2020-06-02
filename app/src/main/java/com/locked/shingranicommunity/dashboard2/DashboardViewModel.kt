package com.locked.shingranicommunity.dashboard2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val session: Session,
    private val navigation: Navigation)
    : ViewModel() {

    companion object {
        val PAGE_EVENTS: Int = 0
        val PAGE_ANNOUNCEMENTS: Int = 1
    }

    private val data: Data = Data()
    val showCreateFab : LiveData<Boolean> = data.showCreateFab
    val currPage: LiveData<Int> = data.currPage

    init {
        data.showCreateFab.value = session.isUserAdmin()
    }

    fun pageChanged(page: Int) {
        data.currPage.postValue(page)
    }

    fun fabPressed() {
        when (currPage.value) {
            PAGE_EVENTS -> navigation.navigateToCreateEvent(true)
            PAGE_ANNOUNCEMENTS -> navigation.navigateToCreateAnnouncement(true)
        }
    }

    private data class Data(
        val currPage: MutableLiveData<Int> = MutableLiveData(PAGE_EVENTS),
        val showCreateFab : MutableLiveData<Boolean> = MutableLiveData(false)
    )
}