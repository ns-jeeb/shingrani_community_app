package com.locked.shingranicommunity.event

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.locked.shingranicommunity.UrCommunityApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.di2.event.EventComponent

class EventActivity : FragmentActivity(), EventComponentProvider {

    override lateinit var eventComponent: EventComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        eventComponent = UrCommunityApplication.instance.appComponent2.eventComponentFactory.create(this)
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