package com.locked.shingranicommunity.dashboard.event.create_event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.SingleTone
import com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent.CreateEventFragment

class CreateItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_item_activity)
        var item = SingleTone.getInstance().getItem()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateEventFragment.newInstance())
                .commitNow()
        }
    }

}
