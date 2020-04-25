package com.locked.shingranicommunity.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject


class DashBoardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepositor,
    val userManager: UserManager)
    :ViewModel() {

    val showCreateFab : LiveData<Boolean>

    init {
        showCreateFab = MutableLiveData<Boolean>(userManager.isAdminUser())
    }

    fun loadItem(context: Context){
        dashboardRepository.fetchEvent()
    }
}