package com.locked.shingranicommunity.navigation

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.auth.Navigation
import com.locked.shingranicommunity.auth.RegisterFragment
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import javax.inject.Inject

class AuthNavigation @Inject constructor(val activity: AppCompatActivity) : Navigation {

    override fun navigateToLogin(addToBackStack: Boolean) {
        NavigationHandler(activity)
            .setFragment(LoginFragment::class.java)
            .setActivity(if (activity is AuthActivity) null else AuthActivity::class.java)
            .addToBackStack(addToBackStack)
            .navigate()
    }

    override fun navigateToRegister(addToBackStack: Boolean) {
        NavigationHandler(activity)
            .setFragment(RegisterFragment::class.java)
            .setActivity(if (activity is AuthActivity) null else AuthActivity::class.java)
            .addToBackStack(addToBackStack)
            .navigate()
    }

    override fun navigateToNext() {
        NavigationHandler(activity)
            .setActivity(DashBoardViewPagerActivity::class.java)
            .navigate()
    }
}