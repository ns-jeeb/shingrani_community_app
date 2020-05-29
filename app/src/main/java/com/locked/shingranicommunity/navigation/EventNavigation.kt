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
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.dashboard2.DashboardActivity
import com.locked.shingranicommunity.event.Navigation
import com.locked.shingranicommunity.models.EventItem
import java.util.*
import javax.inject.Inject


class EventNavigation @Inject constructor(val activity: AppCompatActivity): Navigation {


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

    override fun createFinished() {
        NavigationHandler(activity)
            .setActivity(DashboardActivity::class.java)
            .addToBackStack(false)
            .navigate()
    }

    override fun navigateAutoComplete(addToBackStack: Boolean) {
        if (!Places.isInitialized()) {
            Places.initialize(activity, activity.getString(R.string.google_maps_key), Locale.US)
        }
        val fields = listOf(Place.Field.ID, Place.Field.ADDRESS ,Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(activity)
        startActivityForResult(activity,intent, Constant_Utils.ONE_03,null)
    }

    override fun navigateShare(data: EventItem) {

    }

    override fun navigateToEventDetail(eventId: String) {

    }

}
