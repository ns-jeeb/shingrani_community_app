package com.locked.shingranicommunity.navigation

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.locked.shingranicommunity.search_map.Navigation
import java.util.*
import javax.inject.Inject


class DetailsNavigation @Inject constructor(val activity: AppCompatActivity): Navigation {


    override fun navigateDetails(address: String) {
        TODO("Not yet implemented")
    }
}
