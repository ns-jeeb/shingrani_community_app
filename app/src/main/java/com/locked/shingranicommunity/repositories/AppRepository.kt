package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.locked.shingranicommunity.BuildConfig
import com.locked.shingranicommunity.di2.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.LoginRequestBody
import com.locked.shingranicommunity.models.*
import com.locked.shingranicommunity.session.Session
import com.locked.shingranicommunity.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AppScope
class AppRepository @Inject constructor(
    val apiService: LockedApiService,
    val sessionManager: SessionManager) {

    private val _fetchApp: MutableLiveData<Data> = MutableLiveData<Data>()
    var app: AppModel? = null
    val fetchApp: LiveData<Data> = _fetchApp

    fun fetchApp() {
        val call: Call<AppModel> = apiService.app(BuildConfig.LOCKEDAPI_APP_ID)
        call.enqueue(object: LockedCallback<AppModel>() {
            override fun success(response: AppModel) {
                app = response
                sessionManager.setAdminList(response.admins)
                _fetchApp.postValue(Data(true))
            }
            override fun fail(message: String, details: List<Error>) {
                sessionManager.setAdminList()
                _fetchApp.postValue(Data(false, message))
            }
        })
    }

    data class Data(var success: Boolean = false,
                         var message: String? = null)
}