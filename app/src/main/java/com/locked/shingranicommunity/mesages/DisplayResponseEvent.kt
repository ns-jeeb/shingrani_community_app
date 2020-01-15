package com.locked.shingranicommunity.mesages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject

class DisplayResponseEvent @Inject constructor(val context: Context) : ResponseEvent {
    override fun loginMessageDisplay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerMessageDisplay(username:String) {
//        Toast.makeText(context,"$username created",Toast.LENGTH_LONG).show()
        var intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun itemCreatedMessageDisplay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}