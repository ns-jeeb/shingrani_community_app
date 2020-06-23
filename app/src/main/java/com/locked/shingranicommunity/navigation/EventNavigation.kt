package com.locked.shingranicommunity.navigation

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.dashboard.DashboardActivity
import com.locked.shingranicommunity.details.DetailsActivity
import com.locked.shingranicommunity.details.DetailsFragment
import com.locked.shingranicommunity.event.EventConstants
import com.locked.shingranicommunity.event.Navigation
import com.locked.shingranicommunity.locked.models.EventItem
import com.locked.shingranicommunity.session.SessionManager
import java.util.*
import javax.inject.Inject


class EventNavigation @Inject constructor(val activity: AppCompatActivity,
                                          val sessionManager: SessionManager,
                                          val res: ResourceProvider): Navigation {

    override fun navigateToMap(address: String) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        var uri: String = ""
        val addresses: List<Address> = geocoder.getFromLocationName(address, 1)
        for (i in addresses.indices) {
            uri = java.lang.String.format(Locale.getDefault(), "geo:%f,%f", addresses[i].latitude, addresses[i].longitude)
        }
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("$uri?q=$address")
        }
        if (intent.resolveActivity(activity.packageManager) != null) {
            intent.setPackage("com.google.android.apps.maps")
            startActivity(activity, intent, null)
        }
    }

    override fun navigateSearchAddress(addToBackStack: Boolean) {
        if (!Places.isInitialized()) {
            Places.initialize(activity, res.getString(R.string.google_maps_key))
        }
        val fields = listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(activity)
        startActivityForResult(activity, intent, EventConstants.REQUEST_CODE_SEARCH,null)
        activity.overridePendingTransition(android.R.anim.fade_in, 0)
    }

    override fun navigateShare(data: EventItem) {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody = data.detail
        intent.putExtra(Intent.EXTRA_SUBJECT, data.name + " (" + res.getString(R.string.app_name) + ")")
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        intent.setType("text/plain");
        startActivity(activity, Intent.createChooser(intent, res.getString(R.string.send_to, data.type!!)), null)
    }

    override fun navigateToEventDetail(eventId: String) {
        NavigationHandler(activity)
            .setActivity(DetailsActivity::class.java)
            .setFragment(DetailsFragment::class.java)
            .addToBackStack(true).navigate()
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

    override fun createFinished() {
        NavigationHandler(activity)
            .setActivity(DashboardActivity::class.java)
            .addToBackStack(false)
            .navigate()
    }
}
