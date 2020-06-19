package com.locked.shingranicommunity.search_map

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.repositories.EventRepository
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class ItemDetailsViewModel @Inject constructor(
    private val navigation: Navigation,
    private val resourceProvider: ResourceProvider,
    private val event: EventRepository) : ViewModel(){
    fun searchLocation(locationString: String){

    }
    fun getEventName(){

    }
    fun getEventDetails(){

    }
    fun getDateTime(){

    }
    fun getAttendees(){

    }
    fun accept(){

    }
    fun reject(){

    }
    fun getHeadLine(){

    }
    fun getBackgroundImage(context: Context): Bitmap{
        val imagePath = "ev_weeding_with_cake.png"
        val mngr: AssetManager = context.getAssets()
        var `is`: InputStream? = null
        try {
            `is` = mngr.open(imagePath)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeStream(`is`)
        }
        finally { //Always clear and close
            try {
                `is`?.close()
                `is` = null
            } catch (e: IOException) {
            }
        }
        return bitmap!!
    }
}