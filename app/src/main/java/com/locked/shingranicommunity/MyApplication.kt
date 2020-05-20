package com.locked.shingranicommunity

import android.app.Application
import com.locked.shingranicommunity.di.AppComponent
import com.locked.shingranicommunity.di.DaggerAppComponent

// todo rename to UrCommunityApplication
open class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set
    }
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    val appComponent2: com.locked.shingranicommunity.di2.AppComponent by lazy {
        com.locked.shingranicommunity.di2.DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}