package com.locked.shingranicommunity.registration_login.registration

import android.app.Application
import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.di.AppComponent
import com.locked.shingranicommunity.di.DaggerAppComponent

open class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set
    }
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}