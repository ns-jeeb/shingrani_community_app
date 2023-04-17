package com.locked.shingranicommunity

import android.app.Application
import com.locked.shingranicommunity.di.AppComponent
import com.locked.shingranicommunity.di.DaggerAppComponent

open class UrCommunityApplication : Application() {

    companion object {
        lateinit var instance: UrCommunityApplication
            private set
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}