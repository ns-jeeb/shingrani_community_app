package com.locked.shingranicommunity.dashboard

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.registration_login.registration.user.UserManager
import javax.inject.Inject


class DashBoardViewModel @Inject constructor(private val dashboardRepositor: DashboardRepositor,val userManager: UserManager):ViewModel() {
    fun loadItem(context: Context){
        Toast.makeText(context,"this is working",Toast.LENGTH_LONG).show()
    }
    fun getToken(): String{
        return if (userManager.token.isNotBlank()){
            userManager.token
        }else{
            ""
        }
    }
}