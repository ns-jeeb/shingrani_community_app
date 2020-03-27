package com.locked.shingranicommunity.dashboard.event.create_event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.DashBoardViewModel
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import javax.inject.Inject

class CreateItemActivity : AppCompatActivity() {

    @Inject
    lateinit var  dashBoardViewModel: DashBoardViewModel
    @Inject
    lateinit var dashboardCompunent : DashboardComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardCompunent = (application as MyApplication).appComponent.dashBoardComponent().create()
        dashboardCompunent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_item_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateEventFragment.newInstance())
                .commitNow()
        }
    }

}
