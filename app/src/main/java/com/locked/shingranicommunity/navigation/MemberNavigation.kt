package com.locked.shingranicommunity.navigation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.dashboard.DashboardActivity
import com.locked.shingranicommunity.member.InviteMemberFragment
import com.locked.shingranicommunity.member.MemberActivity
import com.locked.shingranicommunity.member.MemberListFragment
import com.locked.shingranicommunity.member.Navigation
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

class MemberNavigation @Inject constructor(
    val activity: AppCompatActivity,
    val sessionManager: SessionManager,
    val res: ResourceProvider) : Navigation {

    override fun navigateToInvite(addToBackStack: Boolean) {
        NavigationHandler(activity)
            .setActivity(MemberActivity::class.java)
            .setFragment(InviteMemberFragment::class.java)
            .addToBackStack(addToBackStack)
            .navigate()
    }

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

    override fun sendEmail(email: String) {
        val uriText = "mailto:$email" +
                "?body=" + res.getString(R.string.send_email_to_body)
        val uri = Uri.parse(uriText)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(uri)
        activity.startActivity(Intent.createChooser(intent, res.getString(R.string.send_email_to, email)))
    }

    override fun inviteFinished() {
        activity.finish()
    }
}