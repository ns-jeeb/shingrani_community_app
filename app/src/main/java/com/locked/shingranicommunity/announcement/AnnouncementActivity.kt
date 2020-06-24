package com.locked.shingranicommunity.announcement

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.locked.shingranicommunity.UrCommunityApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.di.announcement.AnnouncementComponent

class AnnouncementActivity : FragmentActivity(), AnnouncementComponentProvider {

    override lateinit var announcementComponent: AnnouncementComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        announcementComponent = UrCommunityApplication.instance.appComponent2.announcementComponentFactory.create(this)
        announcementComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
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
        supportFragmentManager.findFragmentById(R.id.fragment)?.onActivityResult(requestCode, resultCode, data)
    }
}