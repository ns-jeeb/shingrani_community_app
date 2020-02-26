package com.locked.shingranicommunity.registration_login.registration

import android.app.Application
import androidx.lifecycle.ViewModel
//import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.di.AppComponent
import com.locked.shingranicommunity.di.DaggerAppComponent
import dagger.MapKey
import kotlin.reflect.KClass

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

    @MustBeDocumented
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value :KClass<out ViewModel>)
}