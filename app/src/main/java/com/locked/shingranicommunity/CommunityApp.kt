package com.locked.shingranicommunity

import android.app.Application
import com.locked.shingranicommunity.tutorials.UserDatabase
import dagger.android.AndroidInjection

class CommunityApp : Application() {

    companion object {
        lateinit var instance: CommunityApp
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}