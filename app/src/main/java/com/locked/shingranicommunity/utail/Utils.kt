package com.locked.shingranicommunity.utail

import android.icu.text.SimpleDateFormat
import java.text.ParseException
import java.util.*

object  Utils {
    fun formatStringDateTime(s: String): String? {
        val sdf = s.split("T", ".")
        val date = sdf[0]
        return "$date : ${formatTime(sdf[1])}"
    }
    private fun formatTime(time:String):String{
        try {
            var pm = false
            val sdf = SimpleDateFormat("H:mm")
            val dateObj: Date = sdf.parse(time)

            pm = time.split(":")[0].toInt()>= 13
            return if (pm){
                "${SimpleDateFormat("K:mm").format(dateObj)} PM"
            }else{
                SimpleDateFormat("K:mm").format(dateObj)
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }
}