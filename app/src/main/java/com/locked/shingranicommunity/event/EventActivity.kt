package com.locked.shingranicommunity.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.MyApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.di2.event.EventComponent

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EventActivity : FragmentActivity(), EventComponentProvider {

    override lateinit var eventComponent: EventComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        eventComponent = MyApplication.instance.appComponent2.eventComponentFactory.create(this)
        eventComponent.inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(R.id.fragment)?.onActivityResult(requestCode,resultCode,data)
    }
}