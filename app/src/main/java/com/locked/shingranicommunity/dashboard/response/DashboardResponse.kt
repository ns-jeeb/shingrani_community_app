package com.locked.shingranicommunity.dashboard.response

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.dashboard.data.Item
import javax.inject.Inject

class DashboardResponse @Inject constructor(private val context: Context): DashboardResponseLister {

    override fun onLoadItems(items: ArrayList<LiveData<Item>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadEvents(items: LiveData<Item>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadAnnouncements(items: MutableLiveData<ArrayList<Item>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}