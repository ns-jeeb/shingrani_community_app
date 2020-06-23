package com.locked.shingranicommunity.navigation

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.member.Navigation
import javax.inject.Inject

class MemberNavigation @Inject constructor(val activity: AppCompatActivity)
    : Navigation {

    override fun navigateToInvite() {
        // todo
    }

    override fun navigateToLogin(clearSession: Boolean) {
        // todo
    }
}