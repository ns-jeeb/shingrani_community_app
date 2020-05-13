package com.locked.shingranicommunity.navigation

import android.content.Intent
import android.content.pm.ResolveInfo
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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

    override fun navigateShare(data: EventItem) {
        TODO("Not yet implemented")
    }

    override fun navigateToEventDetails(eventId: String) {
        TODO("Not yet implemented")
    }

}
