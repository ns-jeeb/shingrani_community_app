package com.locked.shingranicommunity.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.BuildConfig
import com.locked.shingranicommunity.di.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.locked.LockedCallback
import com.locked.shingranicommunity.locked.models.AppModel
import com.locked.shingranicommunity.locked.models.Error
import com.locked.shingranicommunity.session.SessionManager
import retrofit2.Call
import javax.inject.Inject

@AppScope
class AppRepository @Inject constructor(
    val apiService: LockedApiService,
    val sessionManager: SessionManager) {

    private val _fetchApp: MutableLiveData<Data> = MutableLiveData<Data>()
    val fetchApp: LiveData<Data> = _fetchApp
    var app: AppModel? = null

    fun fetchApp() {
        val call: Call<AppModel> = apiService.app(BuildConfig.LOCKEDAPI_APP_ID)
        call.enqueue(object: LockedCallback<AppModel>() {
            override fun success(response: AppModel) {
                app = response
                sessionManager.setTemplateIds(getEventTemplate(response), getAnnouncementTemplate(response))
                sessionManager.setAdminList(response.admins)
                sessionManager.postAppLoaded()
                _fetchApp.postValue(Data(true))
            }
            override fun fail(message: String, details: List<Error>) {
                sessionManager.setAdminList()
                _fetchApp.postValue(Data(false, message))
            }
        })
    }

    private fun getAnnouncementTemplate(app: AppModel): String {
        return app.templates.find { it.title == "Announcement" }?._id ?: ""
    }

    private fun getEventTemplate(app: AppModel): String {
        return app.templates.find { it.title == "Event" }?._id ?: ""
    }

    data class Data(var success: Boolean = false,
                         var message: String? = null)
}