package com.locked.shingranicommunity.navigation

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.settings.Navigation
import javax.inject.Inject

class SettingsNavigation @Inject constructor(val activity: AppCompatActivity): Navigation {

    override fun navigateToLogin() {
        NavigationHandler(activity)
            .setFragment(LoginFragment::class.java)
            .setActivity(AuthActivity::class.java)
            .addToBackStack(false)
            .navigate()
    }
}