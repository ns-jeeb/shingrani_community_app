package com.locked.shingranicommunity.navigation

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.event.Navigation
import java.util.*
import javax.inject.Inject


class EventNavigation @Inject constructor(val activity: AppCompatActivity): Navigation {
    override fun navigateToNext(address: String) {
//        val geocoder = Geocoder(activity, Locale.getDefault())
//        val addresses: List<Address> = geocoder.getFromLocationName("20 Esterbrooke Ave", 1)
//        val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", addresses[0].latitude, addresses[0].longitude)
//        NavigationHandler(activity).showMap(uri, address)

    }

}
