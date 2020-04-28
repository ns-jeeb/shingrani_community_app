package com.locked.shingranicommunity.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.locked.shingranicommunity.R

class NavigationHandler(private val context: AppCompatActivity) {

    companion object {
        val EXTRA_FRAGMENT_CLASS = "EXTRA_FRAGMENT_CLASS"
    }

    private var activity: Class<out AppCompatActivity>? = null
    private var fragment: Class<out Fragment>? = null
    private var addToBackStack: Boolean = true

    fun setActivity(activity: Class<out AppCompatActivity>?): NavigationHandler {
        this.activity = activity
        return this
    }

    fun setFragment(fragment: Class<out Fragment>?): NavigationHandler {
        this.fragment = fragment
        return this
    }

    fun addToBackStack(addToBackStack: Boolean): NavigationHandler {
        this.addToBackStack = addToBackStack
        return this
    }

    fun navigate() {
        if (activity != null) {
            val intent = Intent(context, activity)
            if (fragment != null) {
                intent.putExtra(EXTRA_FRAGMENT_CLASS, fragment)
            }
            if (!addToBackStack) {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        } else if (fragment != null) {
            val fragmentClass: Class<out Fragment>? = fragment
            val transaction: FragmentTransaction = context.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, fragmentClass!!.newInstance())
            if (addToBackStack) {
                transaction.addToBackStack("")
            }
            transaction.commit()
        } else {
            throw IllegalStateException("Can't handle this situation. You must at least provide either activity or fragment to navigate.")
        }
    }
}