package com.locked.shingranicommunity.dashboard2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val session: Session)
    : ViewModel() {

    val showCreateFab : LiveData<Boolean>

    init {
        showCreateFab = MutableLiveData<Boolean>(session.isUserAdmin())
    }
}