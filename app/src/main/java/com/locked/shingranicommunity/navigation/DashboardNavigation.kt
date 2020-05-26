package com.locked.shingranicommunity.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.dashboard.announncement.create_announce.CreateAnnouncementActivity
import com.locked.shingranicommunity.dashboard2.Navigation
import com.locked.shingranicommunity.event.EventActivity
import com.locked.shingranicommunity.event.EventCreateFragment
import javax.inject.Inject

class DashboardNavigation @Inject constructor(val activity: AppCompatActivity): Navigation {
    override fun navigateToCreateEvent(addToBackStack: Boolean) {
        NavigationHandler(activity)
            .setActivity(EventActivity::class.java)
            .setFragment(EventCreateFragment::class.java)
            .addToBackStack(addToBackStack)
            .navigate()
    }

    override fun navigateToCreateAnnouncement(addToBackStack: Boolean) {
        // todo
        val intent = Intent(activity, CreateAnnouncementActivity::class.java)
        activity.startActivity(intent)
    }

}