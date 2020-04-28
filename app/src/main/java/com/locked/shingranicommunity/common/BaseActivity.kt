package com.locked.shingranicommunity.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.lang.IllegalArgumentException

abstract class BaseActivity: AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    fun loadByNavigation() {
        intent.let {
            if (intent.action == "android.intent.action.MAIN") {
                onActivityActionMain()
            }
            val fragmentClass: Serializable? = intent.getSerializableExtra(NavigationHandler.EXTRA_FRAGMENT_CLASS)
            if (fragmentClass != null) {
                NavigationHandler(this)
                    .setFragment(fragmentClass as Class<out Fragment>)
                    .addToBackStack(false)
                    .navigate()
            } else {
                throw IllegalArgumentException("Please provide an AuthFragment")
            }
        }
    }

    open fun onActivityActionMain() {}
}