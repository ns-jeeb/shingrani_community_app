package com.locked.shingranicommunity

import android.app.Application

open class UrCommunityApplication : Application() {

    companion object {
        lateinit var instance: UrCommunityApplication
            private set
    }

    val appComponent2: com.locked.shingranicommunity.di.AppComponent by lazy {
        com.locked.shingranicommunity.di.DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}