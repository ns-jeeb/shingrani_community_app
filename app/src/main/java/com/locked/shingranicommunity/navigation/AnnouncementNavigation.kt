package com.locked.shingranicommunity.navigation

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.announcement.Navigation
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.dashboard.DashboardActivity
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

class AnnouncementNavigation @Inject constructor(val activity: AppCompatActivity,
                                                 val sessionManager: SessionManager,
                                                 val res: ResourceProvider): Navigation {

    override fun navigateToLogin(clearSession: Boolean) {
        if (clearSession) {
            sessionManager.logout()
        }
        NavigationHandler(activity)
            .setFragment(LoginFragment::class.java)
            .setActivity(AuthActivity::class.java)
            .addToBackStack(false)
            .navigate()
    }

    override fun createFinished() {
        NavigationHandler(activity)
            .setActivity(DashboardActivity::class.java)
            .addToBackStack(false)
            .navigate()
    }
}
