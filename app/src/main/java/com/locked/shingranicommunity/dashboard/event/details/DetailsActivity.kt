package com.locked.shingranicommunity.dashboard.event.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.dashboard.event.details.DetailsFragment as DetailsFragment

@SuppressLint("ResourceType")
class DetailsActivity : AppCompatActivity() {

    lateinit var dashboardComponent: DashboardComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardComponent = (application as MyApplication).appComponent.dashBoardComponent().create()
        dashboardComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        var item : Item = intent.getParcelableExtra("extra_item")
        var fragment: DetailsFragment? = supportFragmentManager.findFragmentById(R.id.details_container) as DetailsFragment?
        var transaction: FragmentTransaction? = null
        if (fragment == null){
            fragment  = DetailsFragment.newInstance(item)
        }
        transaction = supportFragmentManager.beginTransaction().replace(R.id.details_container,fragment)
        transaction.commit()
    }
}
